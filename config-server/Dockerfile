FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Копируем JAR файл
RUN apk add --no-cache curl netcat-openbsd

COPY config-server/target/config-server-*.jar app.jar

# Создаем папку для конфигов других сервисов
RUN mkdir -p /app/configs

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]
