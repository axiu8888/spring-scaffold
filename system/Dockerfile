# Docker image for springboot application
# VERSION 1.0.0
# Author: dingxiuan

#VERSION 1.0.0
### 基础镜像，使用alpine操作系统，openjkd使用8u201
FROM openjdk:8u201-jdk-alpine3.9
#作者
MAINTAINER dingxiuan <dingxiuan@163.com>
#系统编码
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
#声明一个挂载点，容器内此路径会对应宿主机的某个文件夹
VOLUME /home/programs/
VOLUME /home/logs/
#应用构建成功后的jar文件被复制到镜像内，名字也改成了app.jar，不拷贝到镜像内，直接通过映射启动
#ADD build/libs/system-1.0.0-SNAPSHOT.jar  /home/program/system/app.jar
ADD start.sh /usr/local/bin
RUN chmod 755 /home/programs/ && chmod 755 /home/logs/  &&  chmod 755 /usr/local/bin/start.sh
# 工作目录
WORKDIR /home/programs/
CMD [ "java", "-Xmx200m", "-Duser.timezone=GMT+08", "-Xdebug", "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=18005", "-jar", "app.jar", "--spring.config.location=./application.properties,./config/conf.properties", "> /home/logs/system-management.log" ]
#启动容器时的进程
ENTRYPOINT ["sh", "/usr/local/bin/start.sh"]
#暴露80端口
EXPOSE 80