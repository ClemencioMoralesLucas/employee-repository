<h1 align="center"> Employee Hierarchy App </h1> <br>
<p align="center">
  <a href="https://gitpoint.co/">
    <img alt="Employee Hierarchy App" title="EmployeeHierarchyApp" src="https://images.indianexpress.com/2020/06/the-office-759-1.jpg" width="450">
  </a>
</p>

<p align="center">
  Just take it easy. We'll fix that hierarchy 😉
</p>

<p align="center">
  <a href="">
    <img alt="Download on the App Store" title="App Store" src="http://i.imgur.com/0n2zqHD.png" width="140">
  </a>

  <a href="">
    <img alt="Get it on Google Play" title="Google Play" src="http://i.imgur.com/mtGRPuM.png" width="140">
  </a>
</p>

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Examples](#examples)
- [Assumptions](#assumptions)
- [Notes](#notes)
- [TODO](#todo)

## Introduction

This microservice, written in ```Java 11```, exposes a ```REST API``` that persist data inside a ```H2 Database``` according to 
the ```Hierarchy GmbH``` specs.

## Features

This repository fulfills the following business-written features:

##### 1
I would like a pure REST API to post the JSON from Chris. I would like to have a properly formatted JSON which reflects the employee hierarchy.
##### 2
I would like the API to be secure so that only I can use it.
##### 3 
Avoid nonsense hierarchies that contain loops or multiple roots.
##### 4 
I would really like it if the hierarchy could be stored in a relational database.
##### 5 
I want to send the name of an employee to an endpoint, and receive the name of the supervisor and the name of the supervisor’s supervisor in return.

## Examples

The whole PostMan collection listed below can be imported from the path ```postman-collection/Employee Hierarchy.postman_collection.json```

##### 1 - Invalid JSON input for POST Organization (POST Endpoint - Unhappy Path - Cycles/Loops detected):

```
{
"Pete": "Nick",
"Jonas": "Sophie",
"Barbara": "Nick",
"Nick": "Sophie",
"Sophie": "Jonas"
}
```

Actual Output:

```
{
    "timestamp": "2021-04-27T16:27:28.660+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "ERROR: Cyclic dependency detected for employee [Jonas]",
    "path": "/api/v1/organization"
}
```

HTTP Response:

<p align="center">
  <a href="https://httpstatusdogs.com/400-bad-request">
    <img alt="Bad Request" title="BadRequest" src="https://httpstatusdogs.com/img/400.jpg" width="450">
  </a>
</p>

##### 2 - Invalid JSON input for POST Organization (POST Endpoint - Unhappy Path - Multiple Roots detected):

```
{
"Pete": "Nick",
"Barbara": "Nick",
"Nick": "Sophie",
"Sophie": "Jonas",
"John": "Sarah"
}
```

Actual Output:

```
{
    "timestamp": "2021-04-28T07:16:08.681+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "ERROR: Multiple roots detected: [Jonas, Sarah]",
    "path": "/api/v1/organization"
}
```

HTTP Response:

<p align="center">
  <a href="https://httpstatusdogs.com/400-bad-request">
    <img alt="Bad Request" title="BadRequest" src="https://httpstatusdogs.com/img/400.jpg" width="450">
  </a>
</p>

##### 3 - Malformed JSON input for POST Organization (POST Endpoint - Unhappy Path - Not a JSON):

```
this is not a JSON!
```

Actual Output:

```
{
    "timestamp": "2021-04-28T07:27:27.281+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "JSON parse error: Unrecognized token 'this': was expecting (JSON String, Number, Array, Object or token 'null', 'true' or 'false'); nested exception is com.fasterxml.jackson.core.JsonParseException: Unrecognized token 'this': was expecting (JSON String, Number, Array, Object or token 'null', 'true' or 'false')\n at [Source: (PushbackInputStream); line: 1, column: 6]",
    "path": "/api/v1/organization"
}
```

HTTP Response:

<p align="center">
  <a href="https://httpstatusdogs.com/400-bad-request">
    <img alt="Bad Request" title="BadRequest" src="https://httpstatusdogs.com/img/400.jpg" width="450">
  </a>
</p>

##### 4 - Empty JSON input for POST Organization (POST Endpoint - Unhappy Path - Empty JSON not to be saved to avoid unnecessary db calls):

```
{}
```

Actual Output:

```
{
    "timestamp": "2021-04-28T07:54:49.529+00:00",
    "status": 400,
    "error": "Bad Request",
    "message": "Invalid or empty JSON",
    "path": "/api/v1/organization"
}
```

HTTP Response:

<p align="center">
  <a href="https://httpstatusdogs.com/400-bad-request">
    <img alt="Bad Request" title="BadRequest" src="https://httpstatusdogs.com/img/400.jpg" width="450">
  </a>
</p>

##### 5 - Valid JSON input for POST Organization (POST Endpoint - Happy Path with sorted supervisor list):

```
{
"Pete": "Nick",
"Barbara": "Nick",
"Nick": "Sophie",
"Sophie": "Jonas"
}
```

Actual Output:

```
{
    "Jonas": {
        "Sophie": {
            "Nick": {
                "Pete": {},
                "Barbara": {}
            }
        }
    }
}
```
HTTP Response:

<p align="center">
  <a href="https://httpstatusdogs.com/200-ok">
    <img alt="OK" title="OK" src="https://httpstatusdogs.com/img/200.jpg" width="450">
  </a>
</p>

##### 6 - Valid JSON input for POST Organization (POST Endpoint - Happy Path with unsorted supervisor list - edge case):

```
{
"Pete": "Nick",
"Barbara": "Nick",
"Nick": "Sophie",
"John": "Nick",
"Sophie": "Jonas"
}
```

Actual Output:

```
{
    "Jonas": {
        "Sophie": {
            "Nick": {
                "Pete": {},
                "Barbara": {},
                "John": {}
            }
        }
    }
}
```
HTTP Response:

<p align="center">
  <a href="https://httpstatusdogs.com/200-ok">
    <img alt="OK" title="OK" src="https://httpstatusdogs.com/img/200.jpg" width="450">
  </a>
</p>

##### 7 - Getting employee-self hierarchy (GET Endpoint after executing - Happy Path with sorted supervisor list):

Query: ```http://localhost:8080/api/v1/organization/employee/Pete/management```

Actual Output:

```
{
    "Pete": {
        "Nick": {
            "Sophie": {
                "Jonas": {}
            }
        }
    }
}
```

HTTP Response:

<p align="center">
  <a href="https://httpstatusdogs.com/200-ok">
    <img alt="OK" title="OK" src="https://httpstatusdogs.com/img/200.jpg" width="450">
  </a>
</p>

##### 8 - Getting employee-self hierarchy (GET Endpoint after executing - Unhappy Path with non-existent name):

Query: ```http://localhost:8080/api/v1/organization/employee/someone/management```

Actual Output:

```
{
    "timestamp": "2021-04-28T08:36:05.271+00:00",
    "status": 404,
    "error": "Not Found",
    "message": "Employee not found",
    "path": "/api/v1/organization/employee/noone/management"
}
```

HTTP Response:

<p align="center">
  <a href="https://httpstatusdogs.com/404-not-found">
    <img alt="Not Found" title="NotFound" src="https://httpstatusdogs.com/img/404.jpg" width="450">
  </a>
</p>

## Assumptions

* Every employee name is unique and cannot be duplicated.
* An empty (```{}```) organization will not be accepted to boost throughput and to avoid an unnecessary database call.
* The system is to be protected for a single user with credentials ```user``` and ```password```. 
Simply putting these two credentials on ```Authorization``` tab (```Basic Auth``` as ```Type``` in 
[PostMan](https://www.postman.com/)) will allow the user to operate the ```REST API```.
 It is important to note that, for simplicity, the password is plain-text on the file, but on a real 
 application this should be encoded 
 (i.e: using ```PasswordEncoderFactories.createDelegatingPasswordEncoder()```)

## Notes

* The branch ```custom-whitelabel``` contains a commit with a mechanism to show HTML pages when 
404, 500 and generic errors appear using the REST API. In order to be more specific regarding
errors (cycles/multiple roots), this work has not been merged into master, but it is available
in the aforementioned branch for further reference. 

* In order to run all tests, ```mvn clean verify``` can be executed from the Terminal.
Apart from executing all tests, it generate an extensive report thanks to 
[JaCoCo](https://www.eclemma.org/jacoco/), that has been
added to the project. After running ```mvn clean verify```, these reports can be analyzed at 
```http://localhost:63342/employee-hierarchy/target/site/jacoco/index.html```

* The H2 Admin Console has been added to simplify any management-related operations
 with the database, and can be accessed through ```http://localhost:8080/h2-console/login.do```
 after launching the application.
 
 * The H2 Database has been indexed via the field ```EMPLOYEE.NAME``` to boost performance.
 It was possible to achieve this index creation in one fell swoop during the init
 script, ```schema.sql```, thanks to adding the flag ```mode=mysql``` to the datasource url in 
 ```application.yml```.

## TODO List

The following aspects are a must-have:
* (+) FINAL CHECK: Ensure all in the PDF works properly

The following aspects would be a nice-to-have:
* (-) Makefile to run springboot? (document it in readme)
* (-) swagger doc
* (-) Script for pushing - get to tha choppa
* (-) fix zipkin warn
* (-) Docker?
* (+) FINAL DOUBLE-CHECK BEFORE DELIVERY: Ensure all in the PDF works properly
