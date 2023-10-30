FROM openjdk:17

WORKDIR /app
COPY /build/libs/*-SNAPSHOT.jar auth.jar

CMD ["java", "-jar", "auth.jar"]