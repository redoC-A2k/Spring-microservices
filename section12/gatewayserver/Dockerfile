# start with a base image containing Java runtime
FROM openjdk:17-jdk-slim
LABEL Maintainer="afshanahmeda2k@gmail.com"
COPY target/gatewayserver-0.0.1-SNAPSHOT.jar gatewayserver-0.0.1-SNAPSHOT.jar
# install curl command
RUN apt-get update && apt-get install -y curl
ENTRYPOINT [ "java", "-jar", "gatewayserver-0.0.1-SNAPSHOT.jar" ]