FROM anapsix/alpine-java:9_jdk

EXPOSE 8080 1099 5005
ADD build/libs/*.jar /opt/service/service.jar
WORKDIR /opt/service

ENTRYPOINT exec java -jar --add-modules java.se.ee \
            ${JAVA_OPTS} \
            -Djava.rmi.server.hostname=${JMX_HOSTNAME:-localhost} \
            -Dcom.sun.management.jmxremote.ssl=false \
            -Dcom.sun.management.jmxremote.authenticate=false \
            -Dcom.sun.management.jmxremote.port=${JMX_PORT:-1099} \
            -Dcom.sun.management.jmxremote.rmi.port=${JMX_PORT:-1099} \
            service.jar

