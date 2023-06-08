FROM gradle:7-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildJar --no-daemon

FROM openjdk:17
EXPOSE 6969:6969
EXPOSE 6963:6963
RUN mkdir /app
RUN mkdir /cert
COPY --from=build /home/gradle/src/cert/* /cert/
COPY --from=build /home/gradle/src/build/libs/whiskerwatch-back.jar /app/WhiskerWatchBack.jar
ENTRYPOINT ["java", "-jar", "/app/WhiskerWatchBack.jar"]