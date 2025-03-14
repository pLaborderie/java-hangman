FROM eclipse-temurin:21 AS build
RUN mkdir /app
COPY . /app
WORKDIR /app
RUN chmod +x ./mvnw
RUN ./mvnw clean install

FROM eclipse-temurin:21
ENV LANGUAGE='en_US:en'
# We make four distinct layers so if there are application changes the library layers can be re-used
COPY --chown=185 --from=build /app/target/quarkus-app/lib/ /deployments/lib/
COPY --chown=185 --from=build /app/target/quarkus-app/*.jar /deployments/
COPY --chown=185 --from=build /app/target/quarkus-app/app/ /deployments/app/
COPY --chown=185 --from=build /app/target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/deployments/quarkus-run.jar" ]