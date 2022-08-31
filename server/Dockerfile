# syntax=docker/dockerfile:experimental
FROM openjdk:18-jdk-bullseye as build
WORKDIR /workspace/app
COPY . /workspace/app
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew bootJar && \
    mv build/libs/colloc-*.jar app.jar

FROM openjdk:18-jdk-bullseye
RUN apt update && apt upgrade -y && apt install -y \
    ca-certificates \
    php7.4 \
    php7.4-curl \
    php7.4-mbstring \
    && apt-get clean && \
    rm -rf /var/lib/apt/lists/*

VOLUME /tmp
RUN wget https://raw.githubusercontent.com/mobilejazz/Colloc/master/colloc.php
COPY --from=build /workspace/app/app.jar .
ENTRYPOINT ["java","-jar","app.jar"]
