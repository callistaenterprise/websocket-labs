websocket-labs
==============

This git repository contains projects that demonstrated various techniques for setting up highly scalable web services and web socket based servers

1. ws-one, demonstrates use of embedded netty and activemq for non-blocking i/o based web-socket support in an existin java-application

2. ws-two, demonstrates use of Java EE 7 and Servlet 3.1's support for non blocking I/O.

3. spring-mvc-asych-teststub, demonstrates use of non-blocking rest services that respond after a configurable time (per request). Tested with 10 000 concurrent users that submit long running requests (5 - 8 sek). For test results see the docs folder.

4. spring-mvc-asych, demonstrates use of non-blocking rest services that route the incoming call to another rest-service. Tested with 5 000 concurrent users that submit long running requests (5 - 8 sek). For test results see the docs folder.
