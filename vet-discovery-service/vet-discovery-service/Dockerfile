# Usa una imagen base de Java con Maven para compilar
FROM maven:3.8.5-openjdk-17 AS build

# Copia todo el código fuente al contenedor
COPY . /app
WORKDIR /app

# Construye el proyecto y crea el archivo JAR
RUN mvn clean install -DskipTests

# --- Segunda etapa: Imagen final ligera para ejecución ---
# Usa una imagen base de Java para ejecutar el JAR
FROM openjdk:17-jdk-slim

# Crea la carpeta de la aplicación
RUN mkdir /app
# Copia el JAR generado en la etapa 'build' al contenedor de ejecución
COPY --from=build /app/target/vet-discovery-service-*.jar /app/app.jar

# Define el puerto que expone la aplicación
EXPOSE 8761

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]