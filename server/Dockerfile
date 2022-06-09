# syntax=docker/dockerfile:experimental
FROM openjdk:18-jdk-alpine as build
WORKDIR /workspace/app
COPY . /workspace/app
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew bootJar && \
    mv build/libs/colloc-*.jar app.jar

FROM openjdk:18-jdk-alpine
RUN apk add --update-cache \
    ca-certificates \
    php7 \
    php7-curl \
    php7-openssl \
    php7-mbstring \
    && rm -rf /var/cache/apk/*
VOLUME /tmp
RUN wget https://raw.githubusercontent.com/mobilejazz/Colloc/master/colloc.php
COPY --from=build /workspace/app/app.jar .
ENTRYPOINT ["java","-jar","app.jar"]