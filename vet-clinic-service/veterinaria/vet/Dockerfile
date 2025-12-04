# --- Etapa 1: Construcción ---
# CAMBIO: Usamos la imagen de Maven con Java 21
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiar pom.xml y descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests

# --- Etapa 2: Ejecución ---
# CAMBIO: Usamos la imagen ligera de Java 21
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiamos el JAR generado
COPY --from=build /app/target/*.jar app.jar

# Puerto donde corre la clínica
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]