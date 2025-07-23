FROM bellsoft/liberica-openjdk-alpine:21

WORKDIR /app
ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:5008"
COPY build/libs/exchange-generator.jar exchange-generator.jar

ENV SERVER_PORT=8083

EXPOSE 8083
EXPOSE 5008

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar exchange-generator.jar"]