FROM bellsoft/liberica-openjdk-alpine:21

WORKDIR /app
ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:5010"
COPY build/libs/gateway.jar gateway.jar

ENV SERVER_PORT=8085

EXPOSE 8085
EXPOSE 5010

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar gateway.jar"]