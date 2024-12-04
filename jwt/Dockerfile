FROM openjdk:21-jdk-slim AS build

WORKDIR /app

#set /jwt/ when using pipeline
COPY /build/libs/jwt.jar /app/jwt.jar

EXPOSE 8081

CMD ["java", "-jar", "/app/jwt.jar"]