FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY api-gateway/target/api-gateway*.jar app.jar

RUN apk add --no-cache curl netcat-openbsd

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
