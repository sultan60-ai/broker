# ---- build stage ----
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Maven wrapper + pom zuerst (bessere Cache Nutzung)
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -q -DskipTests package

# Sources
COPY src/ src/
RUN ./mvnw -q -DskipTests package

# ---- runtime stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Standard: kann je Service überschrieben werden
ENV SERVER_PORT=8090
EXPOSE 8090
ENTRYPOINT ["java","-jar","/app/app.jar"]
