FROM openjdk:21-jdk-slim AS build

WORKDIR /app

#set /jwt/ when using pipeline
COPY /jwt/build/libs/jwt.jar /app/jwt.jar

ARG MONGODB_URI
ENV MONGODB_URI=$MONGODB_URI

ARG GOOGLE_SMTP_PASS
ENV GOOGLE_SMTP_PASS=$GOOGLE_SMTP_PASS

ARG DOMAIN_HOST_ADDRESS
ENV DOMAIN_HOST_ADDRESS=$DOMAIN_HOST_ADDRESS

EXPOSE 8081

CMD ["java", "-jar", "/app/jwt.jar"]