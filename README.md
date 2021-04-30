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
- [Swagger](#swagger)
- [Running the application](#running-the-application)
- [Examples](#examples)
- [Assumptions](#assumptions)
- [Notes](#notes)
- [Scripts](#scripts)
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

## Swagger

In order to use the REST API (and apart from [PostMan](https://www.postman.com/)), the 
[Swagger](https://www.swagger.io/) UI with [Open API](https://www.openapis.org/)
has been added.

Thanks to Swagger (an Interface Description Language for describing RESTful APIs expressed using JSON) the REST API
can be accessed from: ```http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config```

## Running the Application

#### How to run the Spring Boot Application 

* A ```Makefile```  has been added in order to smoothly run the Employee Hierarchy Application by running:
```make run```. This will automatically launch the ```Spring Boot``` application, exposing its
endpoints and making them available. The ```Makefile``` also allows ```make testAll``` in order to run
all the tests except for the Acceptance suite.

* In order to run the ```Acceptance Tests```, the command ```make run``` is to be executed first to 
start the ```Spring Boot``` application. Once it is correctly started, the test 
```src/test/java/com/personio/acceptance/AcceptanceTests.java``` can be executed from an ```IDE```.

* If you prefer to launch the application from your favorite ```IDE``` just launch
  ```src/main/java/com/personio/employeehierarchy/EmployeeHierarchyApplication.java```.  The
  next section in this ```README.md``` file points to the ```POSTMan``` collection you can import to easily
  play with the application. 

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

##### 6 - Getting employee-self hierarchy (GET Endpoint after executing - Happy Path with sorted supervisor list):

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

##### 7 - Getting employee-self hierarchy (GET Endpoint after executing - Unhappy Path with non-existent name):

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

##### 8 - Swagger - Open API Documentation

Query: ```http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config```

Actual Output:

```
<!-- HTML for static distribution bundle build -->
<!DOCTYPE html>
<html lang="en">

<head>
	<meta charset="UTF-8">
	<title>Swagger UI</title>
	<link rel="stylesheet" type="text/css" href="./swagger-ui.css">
	<link rel="icon" type="image/png" href="./favicon-32x32.png" sizes="32x32" />
	<link rel="icon" type="image/png" href="./favicon-16x16.png" sizes="16x16" />
	<style>
		html {
			box-sizing: border-box;
			overflow: -moz-scrollbars-vertical;
			overflow-y: scroll;
		}

		*,
		*:before,
		*:after {
			box-sizing: inherit;
		}

		body {
			margin: 0;
			background: #fafafa;
		}
	</style>
</head>

<body>
	<div id="swagger-ui"></div>

	<script src="./swagger-ui-bundle.js" charset="UTF-8"> </script>
	<script src="./swagger-ui-standalone-preset.js" charset="UTF-8"> </script>
	<script>
		window.onload = function() {
      // Begin Swagger UI call region
      const ui = SwaggerUIBundle({
        url: "https://petstore.swagger.io/v2/swagger.json",
        dom_id: '#swagger-ui',
        deepLinking: true,
        presets: [
          SwaggerUIBundle.presets.apis,
          SwaggerUIStandalonePreset
        ],
        plugins: [
          SwaggerUIBundle.plugins.DownloadUrl
        ],
        layout: "StandaloneLayout"
      })
      // End Swagger UI call region

      window.ui = ui
    }
	</script>
</body>

</html>
```

HTTP Response:

<p align="center">
  <a href="https://httpstatusdogs.com/200-ok">
    <img alt="OK" title="OK" src="https://httpstatusdogs.com/img/200.jpg" width="450">
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
 
## Scripts
 
 The directory ```dotfiles``` is intended for any useful script. It contains
 the BASH script ```GET_TO_DA_CHOPPA``` as a funny way of pushing a new ```commit``` 
 to the ```master```  branch. In a nutshell, the script will only ```commit```
 and subsequently ```push``` if ```mvn clean verify``` finishes with 
 ```BUILD SUCCESS```. 
 
 To run it, just execute the following command from the terminal line:
 
  ```sh dotfiles/GET_TO_DA_CHOPPA.sh <YOUR_COMMIT_MESSAGE>"```
 
 If all the test are running fine, you should hear a cool ```MP3``` voice message, and see the ```ASCII``` art:
 
 ```
                      ______
                    <((((((\\\
                    /      . }\
                    ;--..--._|}
 (\                 '--/\--'  )
  \\                | '-'  :'|
   \\               . -==- .-|
    \\               \.__.'   \--._
    [\\          __.--|       //  _/'--.
    \ \\       .'-._ ('-----'/ __/      \
     \ \\     /   __>|      | '--.       |
      \ \\   |   \   |     /    /       /
       \ '\ /     \  |     |  _/       /
        \  \       \ |     | /        /
         \  \      \        /`
```
 Otherwise...I will let you discover it! 👻
 
 As a side note, the script is inspired on the great action film **Predator**. (A 1987 American 
 science fiction action horror film directed by John McTiernan featuring
 Arnold Schwarzenegger.) 
 
 If you want to remember the ```GET_TO_DA_CHOPPA``` scene, just click on the image
 below to watch the video! 😉
 
 [![Watch the video](https://www.iceposter.com/thumbs/MOV_4b7a51e7_b.jpg)](https://youtu.be/Xs_OacEq2Sk)


## TODO List

The following aspects would be a nice-to-have for the future, but were not implemented yet:
* Dockerize the application.
* Add Zipkin and Sleuth for monitoring.
* Enrich Makefile to run acceptance tests.
