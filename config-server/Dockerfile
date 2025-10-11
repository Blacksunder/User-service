FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN apk add --no-cache curl netcat-openbsd

COPY config-server/target/config-server-*.jar app.jar

RUN mkdir -p /app/configs

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]
