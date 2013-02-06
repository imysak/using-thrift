using-thrift
============

Source of using thrift in Java and node.js. TServer (Java), TClient(Java and node.js).
Java server can be launched from console or(and) under application continer(like Tomcat).

1. Build:
$mvn clean install

2. Start java server:
    $cd java-server/
    $mvn jetty:run

3. Start java client:
    $cd java-client
    $java -Dfile.encoding=UTF-8 -jar target/java-client-1.0-SNAPSHOT.one-jar.jar


