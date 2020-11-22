#!/bin/bash
#export   LANG=C.UTF-8
#source /etc/profile
cd /home/programs/system-management
#appname=$(ls|grep .jar | sort -rn)
appname=$(find ./ -type f -name "*.jar" | sort -rn)
name=$(echo $appname|cut -d ' ' -f1)
java -jar ./$name \
  -Duser.timezone=GMT+08 \
  -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=18001   \
  --spring.config.location=/home/programs/system-management/application.properties,/home/programs/config/conf.properties \
  > /home/logs/system-management.log
