FROM openjdk:21-jdk-slim AS build

WORKDIR /app

COPY jwt/build/libs/jwt.jar /app

EXPOSE 8081

CMD ["java", "-jar", "jwt.jar"]