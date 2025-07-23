FROM bellsoft/liberica-openjdk-alpine:21

WORKDIR /app
ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:5006"
COPY build/libs/blocker.jar blocker.jar

EXPOSE 8081
EXPOSE 5006

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar blocker.jar"]