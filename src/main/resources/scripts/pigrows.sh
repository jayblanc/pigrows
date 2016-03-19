#! /bin/sh
### BEGIN INIT INFO
# Provides: pigrows
# Required-Start:    $local_fs $syslog $remote_fs
# Required-Stop:     
# Default-Start:     2 3 4 5
# Default-Stop:      0 1 6
# Short-Description: Start pigrows lifecycle
### END INIT INFO
#
# pigrows    PiGrows service starting and stopping
#
# Jérôme Blanchard <jayblanc@gmail.com>
#

#set -u

SERVER_URL=http://localhost:8080/pigrows
SERVER_USERNAME=admin
SERVER_PASSWORD=tagada54
DEVICE_KEY=bf60d3
SESSION_ID=$RANDOM
PIGROWS_HOME=/tmp/pigrows
SLAVE_USER=jerome
SLAVE_HOST=localhost
VIEW_BASE_CMD="streamer -c /dev/video0 -b 16" 
#VIEW_BASE_CMD="raspistill -vf -hf"

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
echo "#${SESSION_ID}> connecting to 4G..." >> ${PIGROWS_HOME}/pigrows.log
echo "#${SESSION_ID}> 4G connected." >> ${PIGROWS_HOME}/pigrows.log


#Poster une évènement de démarrage de prise de vue sur le serveur
echo "#${SESSION_ID} (`uptime`)> sending wake up event to server..." >> ${PIGROWS_HOME}/pigrows.log
curl ${SERVER_URL}/api/events/${DEVICE_KEY} -u ${SERVER_USERNAME}:${SERVER_PASSWORD} -d "type=START&message=Starting session ${SESSION_ID}"
echo "#${SESSION_ID}> start event sent to server" >> ${PIGROWS_HOME}/pigrows.log


#Récupérer la commande de prise de vue sur le serveur
echo "#${SESSION_ID} (`uptime`)> getting camera params from server..." >> ${PIGROWS_HOME}/pigrows.log
MASTER_VIEW_PARAMS=$(curl ${SERVER_URL}/api/config/${DEVICE_KEY}/master -u ${SERVER_USERNAME}:${SERVER_PASSWORD})
SLAVE_VIEW_PARAMS=$(curl ${SERVER_URL}/api/config/${DEVICE_KEY}/slave -u ${SERVER_USERNAME}:${SERVER_PASSWORD}) 
VIEW_ID=`date +%Y-%m-%dT%H:%M:%S`
#MASTER_VIEW_CMD="${VIEW_BASE_CMD} ${MASTER_VIEW_PARAMS} -o ${PIGROWS_HOME}/views/${VIEW_ID}.master.jpeg"
MASTER_VIEW_CMD="${VIEW_BASE_CMD} -o ${PIGROWS_HOME}/views/${VIEW_ID}.master.jpeg"
#SLAVE_VIEW_CMD="${VIEW_BASE_CMD} ${SLAVE_VIEW_PARAMS} -o current.jpeg"
SLAVE_VIEW_CMD="${VIEW_BASE_CMD} -o current.jpeg"
echo "#${SESSION_ID}> camera params retreived from server" >> ${PIGROWS_HOME}/pigrows.log


#Se connecter en SSH au slave et récupérer la photo
echo "#${SESSION_ID} (`uptime`)> starting remote picture acquisition..." >> ${PIGROWS_HOME}/pigrows.log
ssh ${SLAVE_USER}@${SLAVE_HOST} $SLAVE_VIEW_CMD
scp ${SLAVE_USER}@${SLAVE_HOST}:~/current.jpeg ${PIGROWS_HOME}/views/${VIEW_ID}.slave.jpeg
echo "#${SESSION_ID}> remote picture acquired" >> ${PIGROWS_HOME}/pigrows.log


#Eteindre l'esclave
echo "#${SESSION_ID} (`uptime`)> shutting down slave..." >> ${PIGROWS_HOME}/pigrows.log
#ssh $SLAVE_USER@$SLAVE_HOST echo "tagada54" | sudo shutdown -h now
echo "#${SESSION_ID}> slave shutdown" >> ${PIGROWS_HOME}/pigrows.log


#Prendre une prise de vue locale
echo "#${SESSION_ID} (`uptime`)> starting local picture acquisition..." >> ${PIGROWS_HOME}/pigrows.log
$MASTER_VIEW_CMD
echo "#${SESSION_ID}> local picture acquired" >> ${PIGROWS_HOME}/pigrows.log


#Déverser les prises de vues non encore versées sur le serveur, et les déplacer dans le dossier d'archive
echo "#${SESSION_ID} (`uptime`)> starting uploading pending pictures..." >> ${PIGROWS_HOME}/pigrows.log
for view in `ls ${PIGROWS_HOME}/views` 
do
	echo "#${SESSION_ID} (`uptime`)> starting picture upload [${PIGROWS_HOME}/views/${view}]..." >> ${PIGROWS_HOME}/pigrows.log
	curl -F file=@${PIGROWS_HOME}/views/${view} ${SERVER_URL}/api/pictures/${DEVICE_KEY} -u admin:tagada54
	mv ${PIGROWS_HOME}/views/${view} ${PIGROWS_HOME}/archives/${view}
	echo "#${SESSION_ID} (`uptime`)> picture upload done [${PIGROWS_HOME}/views/${view}]" >> ${PIGROWS_HOME}/pigrows.log
done
echo "#${SESSION_ID}> all pending pictures uploaded..." >> ${PIGROWS_HOME}/pigrows.log


#Générer un rapport d'état du déroulement du cycle
echo "#${SESSION_ID} (`uptime`)> Everything is done, halting" >> ${PIGROWS_HOME}/pigrows.log
#shutdown -h now


