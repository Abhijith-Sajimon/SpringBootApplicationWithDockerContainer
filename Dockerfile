FROM openjdk:17
LABEL maintainer="Abhijith"
ENV APP_HOME = Users\WIIS\spring_boot_docker_application
WORKDIR $APP_HOME
COPY build/libs/*.jar spring-boot-docker-application.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","spring-boot-docker-application.jar"]
