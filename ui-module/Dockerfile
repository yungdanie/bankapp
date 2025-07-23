FROM bellsoft/liberica-openjdk-alpine:21

WORKDIR /app
ENV JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=0.0.0.0:5013"
COPY build/libs/bankui.jar bankui.jar

ENV SERVER_PORT=8088

EXPOSE 8088
EXPOSE 5013

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar bankui.jar"]