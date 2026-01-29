# ---- build stage ----
FROM eclipse-temurin:17-jdk AS build
WORKDIR /app

# Maven wrapper + pom zuerst (Cache)
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# nur Dependencies laden (kein package!)
RUN ./mvnw -q -DskipTests dependency:go-offline

# Jetzt erst Sources kopieren
COPY src/ src/

# Jetzt bauen (hier sind Sources vorhanden)
RUN ./mvnw -q -DskipTests package

# ---- runtime stage ----
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Standard: kann je Service überschrieben werden
ENV SERVER_PORT=8083
EXPOSE 8083
ENTRYPOINT ["java","-jar","/app/app.jar"]
