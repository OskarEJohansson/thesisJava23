FROM openjdk:21-jdk-slim AS build

WORKDIR /app

#set /admin/ when using pipeline
COPY /admin/build/libs/admin.jar /app/admin.jar

EXPOSE 8082

CMD ["java", "-jar", "/app/admin.jar"]