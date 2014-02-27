Jersey 2 Example
===============

This repository contains a basic example of Jersey v2 in action, showing how to create a basic JSON REST API, and how to test it. 

The example i'm using is of an API representing a set, which allows strings to be added through two different API calls, and which allows them to be requested through another API call.

A lot of examples online use Jersey v1, which causes problems because this version of Jersey uses an entirely different namespace -- v1 uses `com.sun.jersey`, whereas v2 uses `org.glassfish.jersey`, and a number of classes are either named differently, or are in different sub-packages.

The code should work straight out of the box if you're using _Eclipse for Java EE_. There are also a set of unit tests which run against the API and show it's functionality.

### Areas Covered
This example will hopefully be useful if you're interested in one of the following features in relation to jersey:
 - Running in eclipse.
 - Setting up appropriate Jersey Maven dependencies.
 - Setting up your web.xml.
 - Creating a basic API.
 - Using JSON to wrap requests and responses.
 - Using an exception mapper for more readable error handling.
 - Injecting dependencies into resource classes (the API classes).
 - Unit testing Jersey.
 - Mocking calls used by our Jersey API.
 - Unmarshalling responses from a Jersey API.

### Reading the Code
This section describes how the code is structured at a high level. For more specific details on individual lines of code and methods, please read the comments within the relevant classes. 
#### Core
At the core of a Jersey application is the `pom.xml` file, which specifies all of the dependencies in the code, including Jersey itself, and the versions being used.
The `web.xml` file specifies how your servlet is named, and where the main Jersey application class is -- the application class is used to start your Jersey servlet. 

In this example our application class is called `SetApplication`. It registers bindings for a few classes, which we'll discuss later. The key call in this code is the following:
```java
packages(true, "com.aetherworks.example.jersey2.api");
```
This tells the servlet container where to look for the resource classes that form the API. In this case our resource class is called `SetResource`.

#### SetResource (API) Class
This project implements an API which supports two calls to add a string to a set (`/set/add/<value` and `/set/add`, where many values can be added in the body of the request), and a single call to get all entries in the set (`/set/get`). The comments in this class explain the finer points of the annotations that are required, and how they can be used.

The class contains an `@Inject` annotation, which tells the container to inject a dependency into the `callHandler` field. If we look back at the `SetApplication` class, we can see that this value is injected by registering it with the application through the `register()` call.

#### Unit Tests
I've written two unit test classes. One, `SetApiTests` provides what are essentially end-to-end integration tests, which call the API and check that its operations perform as expected. The second, `MockedSetApiTests` provides an example of using mocking to test just the API calls themselves.

Both test classes extends `JerseyTest`, which handles the heavy lifting of setting up a servlet container and providing the API. To run correctly, `JerseyTest` requires a _test framework provider_, which is the servlet container used to run the test. In this example i've used the jetty container, where the dependency is specified in the `pom.xml` class with the following code:
```xml
<dependency>
	<groupId>org.glassfish.jersey.test-framework.providers</groupId>
	<artifactId>jersey-test-framework-provider-jetty</artifactId>
	<version>2.6</version>
</dependency>
```

#### Additional Resources
The following links are resources I found useful in writing this example. 

Where the examples refer to v1 of Jersey I've said so -- these example were typically included because some part of them is useful, but be careful to note places where v1 specific code is used. This includes anywhere where a `com.sun.jersey` namespace is used.

- [Jersey Official Documentation](https://jersey.java.net/documentation/2.6/). The official documentation for Jersey 2.6, which is the version discussed in this repository. If you're reading this in the distant future, you can substitute `latest` for `2.6` to get the documentation for the latest version.
- [Jersey Official Documentation - Test Framework](https://jersey.java.net/documentation/2.6/test-framework.html). Discusses how to set up unit tests with Jersey, and the role of test framework providers.
- [Setting up a Hello World Jersey Application in Eclipse (v1)](http://examples.javacodegeeks.com/enterprise-java/rest/jersey/jersey-hello-world-example/). This is very useful for setting up your own project in eclipse, but be aware that steps 3 and 5 use v1 specific code. 
- [What Makes Parameter Classes Interesting (v1)](http://codahale.com/what-makes-jersey-interesting-parameter-classes/). This discussions further uses of API parameters, beyond what i've attempted in this example.

 