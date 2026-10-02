FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiar el artefacto generado previamente
COPY target/*.jar app.jar

# Puerto de Config Server
EXPOSE 8888

# Optimizacion de memoria JVM para contenedores
ENV JAVA_OPTS="-Xms128m -Xmx256m -XX:+UseG1GC"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
