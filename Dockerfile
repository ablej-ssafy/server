FROM gradle:8.8-jdk21 AS build
WORKDIR /app

COPY src ./src
COPY build.gradle settings.gradle gradle.properties ./
COPY gradle ./gradle

RUN gradle clean build -x test --no-daemon

FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=dev", "/app/app.jar"]