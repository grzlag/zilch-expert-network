FROM azul/zulu-openjdk-alpine:21-jre-headless as base

RUN mkdir /app
WORKDIR /app
COPY build/libs/messagesender-0.0.1-SNAPSHOT.jar /app/messagesender.jar

# Add a non-root user
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser

# Change ownership of the application
RUN chown -R javauser:javauser /app

# Run application as the non-root user
USER javauser

CMD ["java", "-jar", "/app/messagesender.jar"]

EXPOSE 8081