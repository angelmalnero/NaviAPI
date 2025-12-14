# 1. ETAPA DE CONSTRUCCIÓN (Build Stage)
# Usa una imagen que tiene Java 17/21 y Maven para compilar.
FROM maven:latest AS build

# Establece el directorio de trabajo
WORKDIR /app

# Copia todo el código fuente del proyecto
COPY . /app

# COMPILACIÓN: Ejecuta Maven para generar el JAR (el "snapshot")
# ESTO CREA el archivo NavidadAPI-0.0.1-SNAPSHOT.jar en /app/target/
RUN mvn clean package -DskipTests

# ----------------------------------------------------------------------

# 2. ETAPA DE EJECUCIÓN (Runtime Stage)
# Usa una imagen base ligera para ejecutar solo el JAR.
FROM eclipse-temurin:21-jre-alpine

# Copia solo el JAR compilado de la etapa anterior a la etapa final
COPY --from=build /app/target/*.jar /app.jar

# Define el puerto y el punto de entrada
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]