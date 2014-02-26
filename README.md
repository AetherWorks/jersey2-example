Jersey 2 Example
===============

A basic example of Jersey v2. This implements an API representing a set, which allows strings to be added through two different API calls, and which allows them to be requested through another API call.

A lot of examples online use Jersey v1, which causes problems because this version of Jersey uses an entirely different namespace -- v1 uses `com.sun.jersey`, whereas v2 uses `org.glassfish.jersey`, and a number of classes are either named differently, or are in different sub-packages.

The code should work straight out of the box if you're using _Eclipse for Java EE_. There are also a set of unit tests which run against the API and show it's functionality.

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
