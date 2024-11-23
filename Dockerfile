FROM openjdk:17-jdk-alpine3.13

WORKDIR /currency-recruitment

COPY target/currency-recruitment-0.0.1-SNAPSHOT.jar /currency-recruitment

EXPOSE 8080

CMD ["java", "-jar", "currency-recruitment-0.0.1-SNAPSHOT.jar"]