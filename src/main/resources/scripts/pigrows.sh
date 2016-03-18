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

set -u

SERVER_URL=http://pigrows-jayblanc.rhcloud.com
SERVER_USERNAME=pigrows
SERVER_PASSWORD=tagada54
DEVICE_KEY=6e5a0d
SESSION_ID=$RANDOM


#Démarrer le log de la session (assigner un session ID)
echo "Starting session ${SESSION_ID}" >> /var/log/pigrows.log


#Etablir la connexion 3G
echo "#${SESSION_ID}> connecting to 4G..." >> /var/log/pigrows.log
echo "#${SESSION_ID}> 4G connected." >> /var/log/pigrows.log


#Poster une évènement de démarrage de prise de vue sur le serveur
echo "#${SESSION_ID}> sending start event to server..." >> /var/log/pigrows.log
response=$curl ${SERVER_URL}/api/events/${DEVICE_KEY} -u ${SERVER_USERNAME}:${SERVER_PASSWORD} -d "type=START&message=Starting session ${SESSION_ID}"
if [ $? -ne 0 ]; then
  echo "#${SESSION_ID}> ERROR unable to sent event to server !!" >> /var/log/pigrows.log
  ## TODO Try a ping to check server connectivity !!
else
  echo "#${SESSION_ID}> event sent." >> /var/log/pigrows.log
fi


#Récupérer la commande de prise de vue sur le serveur
echo "#${SESSION_ID}> getting camera params from server..." >> /var/log/pigrows.log
curl ${SERVER_URL}/api/config/${DEVICE_KEY}/master -u ${SERVER_USERNAME}:${SERVER_PASSWORD} 
if [ $? -ne 0 ]; then
  echo "#${SESSION_ID}> ERROR unable to get master camera params from server !!" >> /var/log/pigrows.log
else
  echo "#${SESSION_ID}> master camera params retreived." >> /var/log/pigrows.log
fi

VIEW_ID=`date +%Y-%m-%dT%H:%M:%S`

MASTER_VIEW_CMD="raspistill -vf -hf ${MASTER_VIEW_PARAMS} -o ${VIEW_ID}.master.jpg"
SLAVE_VIEW_CMD="raspistill -vf -hf ${SLAVE_VIEW_PARAMS} -o ${VIEW_ID}.master.jpg"


#Se connecter en SSH au slave et récupérer la photo
ssh pi@slave $COMMAND_TAKE_VIEW_SLAVE
scp pi@slave:/home/pi/current.jpg ./views/slave/`FILE FORMAT`.jpg
ssh pi@slave $COMMAND_PURGE_VIEWS_SLAVE $COMMAND_SHUTDOWN

#Prendre une prise de vue locale
$COMMAND_TAKE_VIEW_MASTER ./views/master/`FILE_FORMAT`.jpg

#Déverser les dernières prises de vues sur le serveur


#Vérifier l'intégrité des fichiers versés

#Générer un rapport d'état du déroulement du cycle

#Purger les prises de vues trop anciennes


