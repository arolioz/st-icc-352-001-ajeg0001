# ---------------------------------------------------------------------------
# Imagen Docker multi-etapa para la demo Javalin (Polling / SSE / WebSocket).
#
#   Etapa 1 (build)   -> compila el proyecto y genera el "fat JAR" con Gradle.
#   Etapa 2 (runtime) -> imagen mínima con solo el JRE y el JAR resultante.
#
# Construir:  docker build -t javalin-websocket .
# Ejecutar :  docker run --rm -p 7000:7000 javalin-websocket
# ---------------------------------------------------------------------------

# ----------------------------- Etapa de build ------------------------------
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Copiamos primero el wrapper y los scripts de Gradle para aprovechar la caché
# de capas de Docker: si no cambian, no se vuelven a descargar dependencias.
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew --no-daemon dependencies > /dev/null 2>&1 || true

# Ahora el código fuente y construimos el JAR.
COPY src ./src
RUN ./gradlew --no-daemon shadowJar

# ---------------------------- Etapa de runtime -----------------------------
FROM eclipse-temurin:25-jre
WORKDIR /app

# Ejecutamos como usuario no-root por seguridad.
RUN useradd --system --uid 1001 javalin
USER javalin

# Solo necesitamos el JAR autocontenido generado en la etapa anterior.
COPY --from=build /app/build/libs/app.jar app.jar

# El servidor escucha en 7000 (o en $PORT si se define).
ENV PORT=7000
EXPOSE 7000

ENTRYPOINT ["java", "-jar", "app.jar"]
