# syntax=docker/dockerfile:1.7

# ===== Build stage =====
FROM gradle:8.10.2-jdk17 AS build
WORKDIR /workspace

# Configure Gradle cache location
ENV GRADLE_USER_HOME=/home/gradle/.gradle
# Add HTTP timeouts and IPv4 preference to reduce chances of long stalls during downloads
ENV GRADLE_OPTS="-Dorg.gradle.internal.http.connectionTimeout=60000 -Dorg.gradle.internal.http.socketTimeout=60000 -Djava.net.preferIPv4Stack=true"

# Proxy args (optional). Pass via --build-arg to use corporate proxies during build
ARG HTTP_PROXY
ARG HTTPS_PROXY
ARG NO_PROXY
ENV HTTP_PROXY=${HTTP_PROXY} \
    HTTPS_PROXY=${HTTPS_PROXY} \
    NO_PROXY=${NO_PROXY}

# Copy Gradle wrapper and settings first for better layer caching
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Ensure the gradle wrapper is executable (Linux container) and prime Gradle distribution cache
# Use BuildKit cache mounts so subsequent builds are much faster
RUN --mount=type=cache,target=/home/gradle/.gradle gradle --no-daemon -g /home/gradle/.gradle --info --stacktrace --version

# Pre-fetch dependencies based on build files to avoid long waits during full build
RUN --mount=type=cache,target=/home/gradle/.gradle gradle --no-daemon -g /home/gradle/.gradle --info clean dependencies -x test || true

# Copy source code
COPY src src

# Build the Spring Boot executable jar (skip tests for faster image build)
RUN --mount=type=cache,target=/home/gradle/.gradle gradle --no-daemon -g /home/gradle/.gradle --info bootJar -x test

# ===== Runtime stage =====
FROM eclipse-temurin:17-jre

# Create a non-root user for security
RUN useradd -r -u 1001 appuser
WORKDIR /app

# Copy the built jar from the build stage
# We expect exactly one jar in build/libs/
COPY --from=build /workspace/build/libs/*.jar /app/app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Environment variables that users can override at runtime
ENV JAVA_OPTS="" \
    SPRING_PROFILES_ACTIVE=default

# Healthcheck removed by default to avoid requiring curl/wget in the runtime image
# Enable it later if actuator is exposed and the tool is installed
# HEALTHCHECK --interval=30s --timeout=3s --start-period=20s --retries=3 \
#     CMD curl -fsS http://localhost:8080/actuator/health | grep '"status":"UP"' || exit 1

# Run the JVM
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
