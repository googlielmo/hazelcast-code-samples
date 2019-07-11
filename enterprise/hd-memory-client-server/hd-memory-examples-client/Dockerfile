FROM openjdk:8-jre

ADD target/hd-memory-examples-client-0.1-SNAPSHOT.jar app.jar
ADD target/lib/hazelcast-enterprise-all-3.12-SNAPSHOT.jar hz.jar
ADD target/lib/hazelcast-enterprise-client-3.12-SNAPSHOT.jar hc.jar
ADD target/lib/helper-0.1-SNAPSHOT.jar he.jar
ADD target/lib/cache-api-1.0.0.jar ca.jar
ADD target/lib/HdrHistogram-1.2.1.jar hh.jar

EXPOSE 8010
ENV HZ_LICENSE_KEY ""
ENTRYPOINT ["java","-Dhazelcast.enterprise.license.key=${HZ_LICENSE_KEY}","-Djava.security.egd=file:/dev/./urandom","-cp","/app.jar:/hz.jar:/hc.jar:/he.jar:/ca.jar:/hh.jar", "EnterpriseCacheTestClient"]