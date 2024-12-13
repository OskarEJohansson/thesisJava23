FROM openjdk:21-jdk-slim AS build

WORKDIR /app

#set /library/ when using pipeline
COPY /library/build/libs/library.jar /app/library.jar

ARG MONGODB_URI
ENV MONGODB_URI=$MONGODB_URI

ARG GOOGLE_SMTP_PASS
ENV GOOGLE_SMTP_PASS=$GOOGLE_SMTP_PASS

ARG DOMAIN_HOST_ADDRESS
ENV DOMAIN_HOST_ADDRESS=$DOMAIN_HOST_ADDRESS

EXPOSE 8080

CMD ["java", "-jar", "/app/library.jar"]