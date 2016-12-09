# Screen Casting App
Creates screenshots from the display of the server and exposes them via HTTP(S).

## Build 
```
mvn clean package
```

## Run
```
java -jar target/screen-casting-app.jar
```

## Run with HTTP2
```
java -Xbootclasspath/p:./alpn-boot-8.1.10.v20161026.jar -jar target/screen-casting-app.jar
```

## Configuration

The screen-casting-app listens on port 8443 (https) by default. One can customize the port by setting the  
`server.port` system property, e.g. via `-Dserver.port=1234`. 
