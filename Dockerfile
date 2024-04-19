FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY .env .
COPY pom.xml .

COPY /src ./src
RUN #mvn clean package -DskipTests -Dmaven.repo.local=/usr/share/maven/ref/repository
RUN --mount=type=cache,target=/.m2/repository mvn clean package -DskipTests

FROM eclipse-temurin:17-jre
COPY --from=build /app/target/*.jar application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "application.jar"]

#RUN mvn clean package -DskipTests -Dmaven.repo.local=/usr/share/maven/ref/repository dependency:go-offline
#RUN mvn dependency:go-offline
# Сборка приложения
#FROM maven:3.8.4-openjdk-17 AS build
#WORKDIR /app
#COPY pom.xml .
#RUN mvn dependency:go-offline
#COPY src ./src
#RUN mvn package -DskipTests
#
## Запуск приложения
#FROM openjdk:17-jre-slim
#WORKDIR /app
#COPY --from=build /app/target/my-application.jar application.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "application.jar"]




#FROM maven:3.5.3-jdk-8-alpine as target
#WORKDIR /build
#COPY pom.xml .
#RUN mvn dependency:go-offline
#
#COPY src/ /build/src/
#RUN mvn package
#
## Step : Package image
#FROM openjdk:8-jre-alpine
#EXPOSE 4567
#CMD exec java $JAVA_OPTS -jar /app/my-app.jar
#COPY --from=target /build/target/*jar-with-dependencies.jar /app/my-app.jar


#FROM maven:3.8.5-openjdk-17 AS build
#WORKDIR /
#COPY /src /src
#COPY checkstyle-suppressions.xml /
#COPY pom.xml /
#RUN mvn -f /pom.xml clean package
#
#FROM openjdk:17-jdk-slim
#COPY --from=build /target/*.jar application.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "application.jar"]
#
#
#FROM maven:3.8.4-openjdk-17 as builder
#WORKDIR /app
#COPY . /app/.
#RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip=true
#
#FROM openjdk:17
#WORKDIR /app
#COPY --from=builder /app/target/*.jar /app/*.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "/app/*.jar"]


#---------------------------
#FROM maven:3.8.4-openjdk-17 AS build
#WORKDIR /app
#COPY pom.xml .
#RUN mvn dependency:go-offline
#
#COPY /src ./src
#RUN mvn -f pom.xml clean package -DskipTests
#
#FROM openjdk:17-jre-slim
#COPY --from=build /app/target/*.jar application.jar
#EXPOSE 8080
#ENTRYPOINT ["java", "-jar", "application.jar"]