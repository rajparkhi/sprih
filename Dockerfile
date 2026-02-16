# ---------- STAGE 1: BUILD ----------
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom first (better Docker caching)
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests


# ---------- STAGE 2: RUNTIME ----------
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy only the built jar from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
