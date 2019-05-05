# take from https://spring.io/guides/gs/spring-boot-docker/ 
# Takes an unpacked fat jar builds a docker container out of it. 
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=target/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
# Give the spring application context enough time to start up before polling for health.
HEALTHCHECK --start-period=30s \
  CMD "curl -f http://localhost:8200/health/"
ENTRYPOINT ["java","-cp","app:app/lib/*","us.michaelrhodes.stockkeeper.StockkeeperApplicationKt"]