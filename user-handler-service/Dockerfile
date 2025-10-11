FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache netcat-openbsd

WORKDIR /app

COPY user-handler-service/target/user-handler-service-*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
