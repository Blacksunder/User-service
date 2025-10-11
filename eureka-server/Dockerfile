# Используем официальный образ Java 21
FROM eclipse-temurin:21-jre-alpine

# Создаем директорию приложения
RUN apk add --no-cache curl netcat-openbsd
RUN mkdir -p /app
WORKDIR /app

# Копируем JAR файл в контейнер
COPY eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar app.jar

# Открываем порт
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
