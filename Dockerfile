# 1. IMAGEN BASE
# Usamos una imagen de OpenJDK ligera (slim) con la versión 17 de Java, que es común para Spring Boot.
FROM eclipse-temurin:17-jre-alpine

# 2. DEFINIR VARIABLES DE ENTORNO
# Define el puerto que la aplicación escuchará (el puerto por defecto de Spring Boot).
ENV SERVER_PORT 8080
EXPOSE 8080

# 3. COPIAR EL ARCHIVO JAR
# El archivo JAR ejecutable se crea en la carpeta 'target/' cuando ejecutas 'mvn package'.
# Reemplaza 'nombre-de-tu-artefacto-version.jar' por el nombre real de tu archivo JAR.
# Usa el comodín (*) si no recuerdas la versión exacta.
ARG JAR_FILE=target/*.jar
COPY target/NavidadAPI-0.0.1-SNAPSHOT.jar app.jar

# 4. PUNTO DE ENTRADA
# Esta es la instrucción que se ejecutará al iniciar el contenedor.
# Inicia la JVM y ejecuta el archivo JAR.
ENTRYPOINT ["java", "-jar", "/app.jar"]