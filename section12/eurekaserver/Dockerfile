# start with a base image containing Java runtime
FROM openjdk:17-jdk-slim
LABEL Maintainer="afshanahmeda2k@gmail.com"
COPY target/eurekaserver-0.0.1-SNAPSHOT.jar eurekaserver-0.0.1-SNAPSHOT.jar
RUN apt-get update && apt-get install -y curl
ENTRYPOINT [ "java", "-jar", "eurekaserver-0.0.1-SNAPSHOT.jar" ]