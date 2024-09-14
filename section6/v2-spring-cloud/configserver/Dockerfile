# start with a base image containing Java runtime
FROM openjdk:17-jdk-slim
LABEL Maintainer="afshanahmeda2k@gmail.com"
COPY target/configserver-0.0.1-SNAPSHOT.jar configserver-0.0.1-SNAPSHOT.jar
# install curl command
RUN apt-get update && apt-get install -y curl
ENTRYPOINT [ "java", "-jar", "configserver-0.0.1-SNAPSHOT.jar" ]