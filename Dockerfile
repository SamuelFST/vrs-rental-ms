FROM openjdk:17-alpine AS builder
WORKDIR /app
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x gradlew
RUN ./gradlew test
RUN ./gradlew bootJar
FROM openjdk:17-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /app/
EXPOSE 8043
ENTRYPOINT ["sh", "-c", "java -jar /app/*.jar"]