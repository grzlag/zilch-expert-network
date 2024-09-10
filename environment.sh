#!/bin/bash

start() {
  echo "Starting Docker Compose services..."
  docker-compose up -d

  echo "Starting flagd container..."
  docker run \
    --rm -d \
    --name flagd \
    -p 8013:8013 \
    -v "$(pwd):/etc/flagd" \
    ghcr.io/open-feature/flagd:latest start \
    --uri=file:/etc/flagd/flags.json

  echo "Running Gradle Flyway migrations..."
  cd services/core && ./gradlew flywayMigrate
  cd -  # Return to the original directory

  echo "Environment setup complete!"
}

stop() {
  echo "Stopping Docker Compose services..."
  docker-compose down

  echo "Stopping flagd container..."
  docker stop flagd

  echo "Environment shutdown complete!"
}

if [ "$1" == "start" ]; then
  start
elif [ "$1" == "stop" ]; then
  stop
else
  echo "Usage: $0 {start|stop}"
  exit 1
fi