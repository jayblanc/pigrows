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


#Démarrer le log de la sessions

#Etablir la connexion 3G
??

#Poster une évènement de démarrage de prise de vue sur le serveur


#Récupérer la commande de prise de vue sur le serveur
$COMMAND_TAKE_VIEW_SLAVE
$COMMAND_TAKE_VIEW_MASTER

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


