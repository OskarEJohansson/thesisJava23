FROM openjdk:21-jdk-slim AS build

WORKDIR /app

COPY build/libs/library.jar /app

EXPOSE 8080

CMD ["java", "-jar", "library.jar"]