FROM openjdk:21-jdk-slim AS build

WORKDIR /app

#set /admin/ when using pipeline
COPY /build/libs/admin.jar /app/admin.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/admin.jar"]