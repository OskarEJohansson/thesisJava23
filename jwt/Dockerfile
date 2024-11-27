FROM openjdk:21-jdk-slim AS build

WORKDIR /app

COPY build/libs/jwt.jar /app

EXPOSE 8081

CMD ["java", "-jar", "jwt.jar"]