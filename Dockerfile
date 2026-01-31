# Build stage - usa Java 17
FROM maven:3.9.9-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copiar pom.xml e baixar dependências (melhor cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar código fonte
COPY src ./src

# Build do projeto
RUN mvn clean package -DskipTests

# Runtime stage - usa Java 17
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Criar usuário não-root para segurança
RUN addgroup -S spring && adduser -S spring -G spring

# Copiar JAR do build stage
COPY --from=build /app/target/*.jar app.jar

# Mudar para usuário não-root
RUN chown spring:spring app.jar
USER spring:spring

# Expor porta da aplicação (Render usa a variável PORT)
EXPOSE 8080

# Healthcheck para verificar se a aplicação está funcionando
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/health || exit 1

# Executar aplicação
# Render define a variável PORT, então usamos ${PORT:-8080}
ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -Dspring.profiles.active=${SPRING_PROFILE:-prod} -jar app.jar"]
