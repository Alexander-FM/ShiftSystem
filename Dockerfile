FROM openjdk:21

WORKDIR /app

COPY ./target/turnos-backend-mysql-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "turnos-backend-mysql-0.0.1-SNAPSHOT.jar"]