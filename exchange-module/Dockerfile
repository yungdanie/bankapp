FROM bellsoft/liberica-openjdk-alpine:21

WORKDIR /app
ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:5009"
COPY build/libs/exchange.jar exchange.jar

ENV SERVER_PORT=8084

EXPOSE 8084
EXPOSE 5009

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar exchange.jar"]