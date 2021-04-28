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

##### 3 - Valid JSON input for POST Organization (POST Endpoint - Happy Path with sorted supervisor list):

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

##### 4 - Valid JSON input for POST Organization (POST Endpoint - Happy Path with unsorted supervisor list - edge case):

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

##### 5 - Getting employee-self hierarchy (GET Endpoint after executing - Happy Path with sorted supervisor list):

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

## Assumptions

* Every employee name is unique and cannot be duplicated.
* The system is to be protected for a single user with credentials "user" and "password"

## TODO List

The following aspects would be a nice-to-have:
* (+) Ensure all in the PDF works properly - control {} and invalid JSON for POST and non-existent name for GET 
* (+) auth
* (+) PostMan Collection with use cases (and export it &  link it)
* (+) Acceptance tests
* (+) Refactor getEmployee to its own EmployeeController?
* (+) Explain in the readme mvn clean verify + jacoco + images
* (+) Makefile?
* (+) Docker?
* (+) Script for pushing - get to tha choppa
* (+) swagger doc
* (-) add an index to employee name field in database
