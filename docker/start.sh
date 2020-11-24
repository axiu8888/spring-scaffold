#!/bin/bash
#export   LANG=C.UTF-8
#source /etc/profile
cd /opt/app/
#appname=$(ls|grep .jar | sort -rn)
appname=$(find /opt/app -type f -name "*.jar" | sort -rn)
name=$(echo $appname|cut -d ' ' -f1)
java -jar $name -Duser.timezone=GMT+08 -Xmx300m  -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=18001 --spring.config.location=/opt/app/application.properties,/opt/config/conf.properties > /opt/logs/$name.log
