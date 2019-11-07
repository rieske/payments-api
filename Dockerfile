FROM openjdk:11.0.5-jdk-slim

EXPOSE 8080 1099
WORKDIR /opt/service

ENTRYPOINT exec java -jar \
            --illegal-access=deny \
            ${JAVA_OPTS} \
            -Djava.rmi.server.hostname=${JMX_HOSTNAME:-localhost} \
            -Dcom.sun.management.jmxremote.ssl=false \
            -Dcom.sun.management.jmxremote.authenticate=false \
            -Dcom.sun.management.jmxremote.port=${JMX_PORT:-1099} \
            -Dcom.sun.management.jmxremote.rmi.port=${JMX_PORT:-1099} \
            service.jar

ADD build/libs/payments-api.jar /opt/service/service.jar
