# ==========================
# Fase 1: Build Stage
# ==========================
FROM eclipse-temurin:21-jdk AS buildstage

# Instalar Maven para la compilación
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copiar el archivo de configuración de Maven (pom.xml) primero para aprovechar el caché
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiar el código fuente y el wallet
COPY src /app/src
COPY wallet /app/wallet

# Ejecutar la compilación
RUN mvn clean package -DskipTests

# ==========================
# Fase 2: Runtime Stage
# ==========================
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copiar el archivo JAR generado
COPY --from=buildstage /app/target/usuarioservice-0.2.0-SNAPSHOT.jar /app/usuarioservice.jar

# Copiar el wallet para la conexión a Oracle
COPY wallet /app/wallet

# Exponer el puerto
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/usuarioservice.jar"]
