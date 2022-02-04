FROM gradle:4.7.0-jdk8-alpine AS GRADLE_BUILD
MAINTAINER Karthik Siva
COPY --chown=gradle:gradle . /home/gradle/tms-api
WORKDIR /home/gradle/tms-api
RUN chmod 777 gradlew
RUN ./gradlew clean build

FROM openjdk:8-jre-alpine3.9
EXPOSE 8082
COPY --from=GRADLE_BUILD /home/gradle/tms-api/tms/build/libs/*.jar /app/
CMD ["java", "-Dspring.profiles.active=prod", "-jar", "app/tms.jar"]