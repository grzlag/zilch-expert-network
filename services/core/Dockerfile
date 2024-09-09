FROM azul/zulu-openjdk-alpine:21-jre-headless as base

RUN mkdir /app
WORKDIR /app
COPY build/libs/core*SNAPSHOT.jar /app/core.jar

# Add a non-root user
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser

# Change ownership of the application
RUN chown -R javauser:javauser /app

# Run application as the non-root user
USER javauser

CMD ["java", "-jar", "/app/core.jar"]

EXPOSE  8080