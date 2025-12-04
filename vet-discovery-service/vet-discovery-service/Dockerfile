# --- Primera etapa: Construcción (Build) ---
# CAMBIO: Usamos una imagen de Maven basada en Eclipse Temurin (más estable y moderna)
FROM maven:3.9.9-eclipse-temurin-17 AS build

WORKDIR /app

# 1. Copiamos primero el pom.xml y descargamos dependencias.
COPY pom.xml .
RUN mvn dependency:go-offline

# 2. Copiamos el código fuente y compilamos
COPY src ./src
RUN mvn clean package -DskipTests

# --- Segunda etapa: Ejecución (Run) ---
# CAMBIO: La imagen 'openjdk' está deprecada. Usamos eclipse-temurin JRE (más ligera, solo para correr Java).
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiamos el JAR generado.
COPY --from=build /app/target/vet-discovery-service-*.jar app.jar

# Puerto por defecto de Eureka Server
EXPOSE 8761

# Ejecutamos la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]