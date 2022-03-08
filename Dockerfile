FROM openjdk:17
LABEL maintainer="Abhijith"
COPY build/libs/*.jar spring-boot-docker-application.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","spring-boot-docker-application.jar"]
