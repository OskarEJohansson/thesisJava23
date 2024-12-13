FROM openjdk:21-jdk-slim AS build

WORKDIR /app

#set /admin/ when using pipeline
COPY /admin/build/libs/admin.jar /app/admin.jar

ARG MONGODB_URI
ENV MONGODB_URI=$MONGODB_URI

ARG GOOGLE_SMTP_PASS
ENV GOOGLE_SMTP_PASS=$GOOGLE_SMTP_PASS

ARG DOMAIN_HOST_ADDRESS
ENV DOMAIN_HOST_ADDRESS=$DOMAIN_HOST_ADDRESS

EXPOSE 8082

CMD ["java", "-jar", "/app/admin.jar"]