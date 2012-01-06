Introduction
============
This quickstart demonstrates the usage of the HornetQ Component and it's binding feature, 
by binding to a HornetQ Queue. When a message arrives in this queue the service will be invoked.

JBoss AS 7
----------
1. Build the quickstart:
    mvn clean install
2. Start JBoss AS 7 in standalone mode:
     ./standalone.sh -server-config standalone-preview.xml
3. Start JBoss CLI and connect: 
    ./jboss-admin.sh --connect
4. Create the JMS Queue using CLI:
    add-jms-queue --name=GreetingServiceQueue
5. Deploy the quickstart
     deploy  /path/to/quickstarts/hornetq-binding/target/switchyard-quickstarts-hornetq-binding-{version}-SNAPSHOT.jar
6. Execute HornetQClient
    mvn exec:java 
7. Check the server console for output from the service.

Expected Results
================
```
14:20:30,063 INFO  [org.jboss.as.server.controller]
(DeploymentScanner-threads - 2) Deployed "switchyard-quickstarts-hornetq-binding-0.4.0-SNAPSHOT.jar"  
14:20:46,268 INFO  [org.switchyard.component.hornetq.deploy.InboundHandler]
(Thread-1 (group:HornetQ-client-global-threads-9176206)) onMessage :ClientMessage[messageID=9, durable=false, address=jms.queue.GreetingServiceQueue,properties=TypedProperties[null]]  
14:20:46,280 INFO  [stdout]
(Thread-1 (group:HornetQ-client-global-threads-9176206)) Hello there Captain Crunch :-)
``` 

## Further Reading

1. [HornetQ Bindings Documentation](https://docs.jboss.org/author/display/SWITCHYARD/HornetQ+Bindings)
