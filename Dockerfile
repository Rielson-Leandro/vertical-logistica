# Use a imagem base do OpenJDK
FROM openjdk:17-jdk-alpine

# Defina o diretório de trabalho
WORKDIR /app

# Copie o arquivo JAR da aplicação para o contêiner
COPY build/libs/vertical-logistica-0.0.1-SNAPSHOT.jar app.jar

# Exponha a porta em que a aplicação será executada
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
