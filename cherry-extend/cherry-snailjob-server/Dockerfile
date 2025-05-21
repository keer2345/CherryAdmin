# 贝尔实验室 Spring 官方推荐镜像 JDK下载地址 https://bell-sw.com/pages/downloads/
FROM bellsoft/liberica-openjdk-debian:17.0.11-cds
#FROM bellsoft/liberica-openjdk-debian:21.0.5-cds
#FROM findepi/graalvm:java17-native

LABEL maintainer="Cherry Admin"

RUN mkdir -p /cherry/monitor/logs

WORKDIR /cherry/snailjob

COPY ./target/cherry-snailjob-server.jar ./app.jar

SHELL ["/bin/bash", "-c"]

ENV LANG=C.UTF-8 LC_ALL=C.UTF-8 JAVA_OPTS=""
## 设置 JAVA_OPTS 环境变量，可通过 docker run -e "JAVA_OPTS=" 进行覆盖
ENV JAVA_OPTS="-Xms512m -Xmx512m -Djava.security.egd=file:/dev/./urandom"
## 应用参数
ENV ARGS=""

EXPOSE 6800

## 启动后端项目
CMD java ${JAVA_OPTS} -jar app.jar $ARGS
