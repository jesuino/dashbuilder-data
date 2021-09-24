This is a sample for using a Java class to generate a dataset. It also contais a sample dashboard generated using Java.

### Build

Run maven to build the project

`mvn clean install`

To generate the dashboard:

mvn compile exec:java -Dexec.mainClass="org.dashbuilder.SocialNetworkDashboard"

A file `social-networks-dashboard.zip` will be generated.

### Running

To run the dashboard you need DashBuilder Runtime and make sure that the JAR is in classpath. Here's the command

```
java  -cp 'target/sample-bean-dataset-0.0.1-SNAPSHOT.jar:/path/to/dashbuilder-runtime-app.jar' io.quarkus.runner.GeneratedMain 
```

Then with a web browser go to `localhost:8080` and upload `dashboard.zip` or set the path to dashboard.zip when starting DB Runtime:

```
java -Ddashbuilder.runtime.import=/path/to/social-networks-dashboard.zip -cp 'target/sample-bean-dataset-0.0.1-SNAPSHOT.jar:/path/to/dashbuilder-runtime-app.jar' io.quarkus.runner.GeneratedMain 
```

