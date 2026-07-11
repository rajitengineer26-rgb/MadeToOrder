# ✅ Base image (MANDATORY)
FROM eclipse-temurin:21-jdk

# ✅ Working directory
WORKDIR /app

# ✅ Copy correct JAR (IMPORTANT)
COPY build/libs/madetoorder-0.0.1-SNAPSHOT.jar app.jar

# ✅ Expose port
EXPOSE 8080

# ✅ Run application
ENTRYPOINT ["java", "-jar", "app.jar"]