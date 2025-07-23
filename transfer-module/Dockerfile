FROM bellsoft/liberica-openjdk-alpine:21

WORKDIR /app
ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:5012"
COPY build/libs/transfer.jar transfer.jar

ENV SERVER_PORT=8087

EXPOSE 8087
EXPOSE 5012

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar transfer.jar"]