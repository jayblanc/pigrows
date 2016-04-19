#! /bin/sh
#
# pigrows    PiGrows bootup cron program

#
# Jérôme Blanchard <jayblanc@gmail.com>
#

#set -u

SERVER_HOST=pigrows-jayblanc.rhcloud.com
SERVER_URL=http://pigrows-jayblanc.rhcloud.com
SERVER_USERNAME=pigrows
SERVER_PASSWORD=tagada54
DEVICE_KEY=bf60d3
SESSION_ID=`< /dev/urandom tr -dc _A-Z-a-z-0-9 | head -c${1:-8}`
#PIGROWS_HOME=/tmp/pigrows
PIGROWS_HOME=/home/pi/pigrows
SLAVE_USER=pi
SLAVE_HOST=localhost
#VIEW_BASE_CMD="streamer -c /dev/video0 -b 16" 
VIEW_BASE_CMD="raspistill -vf -hf"
MODE=offline
ARCHIVE=false


#Créer les dossiers qui n'existeraient pas
if [ ! -d $PIGROWS_HOME/views ]; then
    mkdir -p $PIGROWS_HOME/views;
fi
if [ ! -d $PIGROWS_HOME/archives ]; then
    mkdir -p $PIGROWS_HOME/archives;
fi


#Démarrer le log de la session (assigner un session ID)
echo "Starting session ${SESSION_ID}" >> ${PIGROWS_HOME}/pigrows.log


#Etablir la connexion 3G
echo "#${SESSION_ID}> connecting to 3G..." >> ${PIGROWS_HOME}/pigrows.log
sudo ifup ppp0
for i in `seq 1 3`
do
  sleep 10
  if ping -q -c 1 -W 1 google.com >/dev/null; then
    echo "#${SESSION_ID}> 3G connected, google reachable." >> ${PIGROWS_HOME}/pigrows.log
    MODE=online
    break
  fi
  echo "#${SESSION_ID}> 3G NOT connected, attempt ${i}/3." >> ${PIGROWS_HOME}/pigrows.log
  if [ $i = '3' ]
  then 
    echo "#${SESSION_ID}> Network unreachable, going to offline mode." >> ${PIGROWS_HOME}/pigrows.log
  fi
done


#Si on est online, poster une évènement de démarrage de prise de vue sur le serveur, sinon simplement logger
if [ $MODE = 'online' ]
then
  echo "#${SESSION_ID} (`uptime`)> sending wake up event to server..." >> ${PIGROWS_HOME}/pigrows.log
  curl ${SERVER_URL}/api/events/${DEVICE_KEY} -u ${SERVER_USERNAME}:${SERVER_PASSWORD} -d "type=START&message=Starting session ${SESSION_ID}"
  echo "#${SESSION_ID}> start event sent to server" >> ${PIGROWS_HOME}/pigrows.log
else
  echo "#${SESSION_ID}> (offline) starting session" >> ${PIGROWS_HOME}/pigrows.log
fi


#Si on est online, récupérer la commande de prise de vue sur le serveur, sinon prendre la config par defaut
VIEW_ID=L${DEVICE_KEY}_D`date +%Y%m%d_T%H:%M:%S`
if [ $MODE = 'online' ]
then
  echo "#${SESSION_ID} (`uptime`)> getting camera params from server..." >> ${PIGROWS_HOME}/pigrows.log
  MASTER_VIEW_PARAMS=$(curl ${SERVER_URL}/api/config/${DEVICE_KEY}/master -u ${SERVER_USERNAME}:${SERVER_PASSWORD})
  SLAVE_VIEW_PARAMS=$(curl ${SERVER_URL}/api/config/${DEVICE_KEY}/slave -u ${SERVER_USERNAME}:${SERVER_PASSWORD})
  echo "#${SESSION_ID}> camera params retreived from server" >> ${PIGROWS_HOME}/pigrows.log
else
  echo "#${SESSION_ID}> (offline) default camera params used" >> ${PIGROWS_HOME}/pigrows.log
  MASTER_VIEW_PARAMS=-q 95 -sh 0 -co 0 -br 50 -sa 0 -ISO 100 -ev 0 -ex auto -awb auto
  SLAVE_VIEW_PARAMS=$MASTER_VIEW_PARAMS
fi
MASTER_VIEW_CMD="${VIEW_BASE_CMD} ${MASTER_VIEW_PARAMS} -o ${PIGROWS_HOME}/views/${VIEW_ID}_M.jpg"
#MASTER_VIEW_CMD="${VIEW_BASE_CMD} -o ${PIGROWS_HOME}/views/${VIEW_ID}_M.jpg"
SLAVE_VIEW_CMD="${VIEW_BASE_CMD} ${SLAVE_VIEW_PARAMS} -o current.jpg"
#SLAVE_VIEW_CMD="${VIEW_BASE_CMD} -o current.jpg"


#Se connecter en SSH au slave et récupérer la photo
echo "#${SESSION_ID} (`uptime`)> starting remote picture acquisition..." >> ${PIGROWS_HOME}/pigrows.log
ssh ${SLAVE_USER}@${SLAVE_HOST} $SLAVE_VIEW_CMD
scp ${SLAVE_USER}@${SLAVE_HOST}:~/current.jpg ${PIGROWS_HOME}/views/${VIEW_ID}_S.jpg
echo "#${SESSION_ID}> remote picture acquired" >> ${PIGROWS_HOME}/pigrows.log


#Eteindre l'esclave
echo "#${SESSION_ID} (`uptime`)> shutting down slave..." >> ${PIGROWS_HOME}/pigrows.log
#ssh ${SLAVE_USER}@${SLAVE_HOST} sudo shutdown -h now
echo "#${SESSION_ID}> slave shutdown" >> ${PIGROWS_HOME}/pigrows.log


#Prendre une prise de vue locale
echo "#${SESSION_ID} (`uptime`)> starting local picture acquisition..." >> ${PIGROWS_HOME}/pigrows.log
$MASTER_VIEW_CMD
echo "#${SESSION_ID}> local picture acquired" >> ${PIGROWS_HOME}/pigrows.log


#Déverser les prises de vues non encore versées sur le serveur, et les déplacer dans le dossier d'archive
if [ $MODE = 'online' ] 
then
  echo "#${SESSION_ID} (`uptime`)> starting uploading pending pictures..." >> ${PIGROWS_HOME}/pigrows.log
  for view in `ls ${PIGROWS_HOME}/views`
  do
    echo "#${SESSION_ID} (`uptime`)> starting picture upload [${PIGROWS_HOME}/views/${view}]..." >> ${PIGROWS_HOME}/pigrows.log
    curl -F file=@${PIGROWS_HOME}/views/${view} ${SERVER_URL}/api/pictures/${DEVICE_KEY} -u ${SERVER_USERNAME}:${SERVER_PASSWORD}
    echo "#${SESSION_ID} (`uptime`)> picture upload done [${PIGROWS_HOME}/views/${view}]" >> ${PIGROWS_HOME}/pigrows.log
    if [ $ARCHIVE = 'true' ] 
    then
      mv ${PIGROWS_HOME}/views/${view} ${PIGROWS_HOME}/archives/${view}
      echo "#${SESSION_ID} (`uptime`)> picture archived [${PIGROWS_HOME}/views/${view}]" >> ${PIGROWS_HOME}/pigrows.log
    else
      rm ${PIGROWS_HOME}/views/${view}
      echo "#${SESSION_ID} (`uptime`)> picture destroyed [${PIGROWS_HOME}/views/${view}]" >> ${PIGROWS_HOME}/pigrows.log
    fi  
  done
  echo "#${SESSION_ID}> all pending pictures treated..." >> ${PIGROWS_HOME}/pigrows.log
fi

#Si on est online, poster une évènement de fin du cycle, sinon simplement logger
if [ $MODE = 'online' ]
then
  echo "#${SESSION_ID} (`uptime`)> sending halt event to server..." >> ${PIGROWS_HOME}/pigrows.log
  curl ${SERVER_URL}/api/events/${DEVICE_KEY} -u ${SERVER_USERNAME}:${SERVER_PASSWORD} -d "type=STOP&message=Halting session ${SESSION_ID}"
  echo "#${SESSION_ID} (`uptime`)> halt event sent to server" >> ${PIGROWS_HOME}/pigrows.log
else
  echo "#${SESSION_ID} (`uptime`)> (offline) Everything is done, halting" >> ${PIGROWS_HOME}/pigrows.log
fi
sudo shutdown -h now



