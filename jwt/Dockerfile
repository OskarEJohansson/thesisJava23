FROM openjdk:21-jdk-slim AS build

WORKDIR /app

#set /jwt/ when using pipeline
COPY /jwt/build/libs/jwt.jar /app/jwt.jar

ARG MONGODB_URI
ENV MONGODB_URI=$MONGODB_URI

EXPOSE 8081

CMD ["java", "-jar", "/app/jwt.jar"]