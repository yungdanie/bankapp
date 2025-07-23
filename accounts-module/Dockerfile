FROM bellsoft/liberica-openjdk-alpine:21

WORKDIR /app
ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:5005"
COPY build/libs/accounts.jar accounts.jar

ENV SERVER_PORT=8080

EXPOSE 8080
EXPOSE 5005

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar accounts.jar"]