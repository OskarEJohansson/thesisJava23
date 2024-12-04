FROM openjdk:21-jdk-slim AS build

WORKDIR /app

#set /library/ when using pipeline
COPY /library/build/libs/library.jar /app/library.jar

ARG MONGODB_URI
ENV MONGODB_URI=$MONGODB_URI

EXPOSE 8080

CMD ["java", "-jar", "/app/library.jar"]