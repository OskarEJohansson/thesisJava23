FROM openjdk:21-jdk-slim AS build

WORKDIR /app

COPY /build/libs/library.jar /app/library.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/library.jar"]