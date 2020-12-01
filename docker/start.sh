#!/bin/bash
#export   LANG=C.UTF-8
#source /etc/profile
cd /opt/app/
#appname=$(ls|grep .jar | sort -rn)
appname=$(find ./ -type f -name "*.jar" | sort -rn)
name=$(echo $appname|cut -d ' ' -f1)
java -jar ./$name \
  -Duser.timezone=GMT+08 \
  -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=15001   \
  --spring.config.location=./application.properties \
  > /opt/logs/$(echo "$name" | cut -f 1 -d '.').log
