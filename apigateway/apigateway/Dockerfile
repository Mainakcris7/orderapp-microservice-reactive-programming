FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY src src
COPY gradle gradle
COPY build.gradle .
COPY gradlew .
COPY gradlew.bat .
COPY settings.gradle .
RUN chmod +x gradlew
RUN ./gradlew clean bootJar -x test

FROM eclipse-temurin:21-jre-ubi9-minimal
COPY --from=build /app/build/libs/*.jar apigateway.jar
EXPOSE 7777
ENTRYPOINT ["java", "-jar", "apigateway.jar"]