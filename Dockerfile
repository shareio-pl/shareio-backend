FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY pom.xml .

COPY bootstrap/pom.xml bootstrap/
COPY lib/pom.xml lib/
COPY location/pom.xml location/
COPY offer/pom.xml offer/
COPY score/pom.xml score/
COPY user/pom.xml user/

RUN mvn dependency:resolve-plugins

COPY bootstrap/src/main bootstrap/src/main
COPY lib/src/main lib/src/main
COPY location/src/main location/src/main
COPY offer/src/main offer/src/main
COPY score/src/main score/src/main
COPY user/src/main user/src/main

RUN mvn package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/bootstrap/target/bootstrap-0.0.1-SNAPSHOT.war .

EXPOSE 8081

CMD ["java", "-jar", "bootstrap-0.0.1-SNAPSHOT.war"]
