FROM eclipse-temurin:17-jre-alpine

COPY build/libs/music-metadata-service-1.0.0.jar /usr/local/lib/app.jar

EXPOSE 8081

CMD ["java", "-jar", "/usr/local/lib/app.jar"]