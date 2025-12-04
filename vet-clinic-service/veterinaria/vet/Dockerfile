# --- Etapa 1: Construcción ---
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar pom.xml y descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests

# --- Etapa 2: Ejecución ---
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiamos el JAR generado (busca cualquiera que termine en .jar en target)
COPY --from=build /app/target/*.jar app.jar

# Puerto donde corre la clínica (usualmente 8080 o 8081, lo ajustamos con variables)
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]