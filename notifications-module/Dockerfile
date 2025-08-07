FROM bellsoft/liberica-openjdk-alpine:21

WORKDIR /app
ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:5011"
COPY build/libs/notifications.jar notifications.jar

ENV SERVER_PORT=8086

EXPOSE 8086
EXPOSE 5011

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar notifications.jar"]