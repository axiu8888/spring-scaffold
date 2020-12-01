# Docker image for springboot application
# VERSION 1.0.0
# Author: dingxiuan

#VERSION 1.0.0

### 基础镜像，使用alpine操作系统，openjkd使用8u201
#FROM openjdk:8u201-jdk-alpine3.9
FROM openjdk:15
#作者
MAINTAINER dingxiuan <dingxiuan@163.com>

# 版本
LABEL version=1.0  description=openjdk15

#系统编码
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
#声明一个挂载点，容器内此路径会对应宿主机的某个文件夹
VOLUME /opt/app/
VOLUME /opt/logs/
VOLUME /usr/local/bin/
#应用构建成功后的jar文件被复制到镜像内，名字也改成了app.jar，不拷贝到镜像内，直接通过映射启动
#ADD build/libs/app.jar  /home/program/app.jar
RUN mkdir -p /usr/local/bin/
ADD start.sh /usr/local/bin/start.sh
RUN chmod 755 /opt/app/ && chmod 755 /opt/logs/ && chmod 755 /usr/local/bin/start.sh
# 工作目录
WORKDIR /opt/app/
#CMD [ \
#      "java", \
#      "-Xmx300m", \
#      "-Duser.timezone=GMT+08", \
#      "-Xdebug", \
#      "-Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=18001", \
#      "-jar", \
#      "/opt/app/app.jar", \
#      "--spring.config.location=/opt/app/application.properties,/opt/config/conf.properties" \
#    ]
#启动容器时的进程
ENTRYPOINT ["sh", "/usr/local/bin/start.sh"]

HEALTHCHECK --interval=30s --timeout=30s CMD curl -f http://localhost:80/actuator/health || exit 1

#暴露80端口
EXPOSE 80/tcp
#EXPOSE 80/udp


#--build-arg=[] :设置镜像创建时的变量；
#-f :指定要使用的Dockerfile路径；
#--force-rm :设置镜像过程中删除中间容器；
#--rm :设置镜像成功后删除中间容器；
#--tag, -t: 镜像的名字及标签，通常 name:tag 或者 name 格式；
# 拷贝Dockerfile和start.sh文件到centos，然后执行如下命令
# docker build -f Dockerfile --rm  --tag openjdk15:1.0 .