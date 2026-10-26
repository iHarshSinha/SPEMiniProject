# Spring Boot Calculator API

A RESTful calculator API built with Spring Boot 4 and Java 21. It
supports addition, subtraction, multiplication, and division using
decimal numbers. The API returns JSON responses and provides centralized
error handling for invalid requests.

## Technologies

-   Java 21
-   Spring Boot 4.0.9
-   Maven
-   Spring MVC
-   Spring Boot Actuator
-   JUnit 5 and MockMvc

## Prerequisites

-   Java 21 or later
-   Maven, or the included Maven Wrapper (`./mvnw`)

Check your Java installation:

``` bash
java -version
```

## Build and test

From the project root, run the tests:

``` bash
./mvnw test
```

Build and verify the project, including tests:

``` bash
./mvnw clean verify
```

To package the executable JAR:

``` bash
./mvnw clean package
```

Maven test reports are generated in `target/surefire-reports/`,
including XML reports suitable for test-reporting tools.

## Run the application

Start the application using Maven:

``` bash
./mvnw spring-boot:run
```

Alternatively, build the JAR and run it directly:

``` bash
java -jar target/MiniProjectSPE-0.0.1-SNAPSHOT.jar
```

By default, the application is available at `http://localhost:8080`.

## Calculator API

All calculator endpoints use **HTTP GET** and require two query
parameters: `a` and `b`. Both parameters must be valid numbers.

  Method   Endpoint                     Description
  -------- ---------------------------- ------------------------
  GET      `/api/v1/add?a=2&b=3`        Adds `a` and `b`
  GET      `/api/v1/subtract?a=5&b=3`   Subtracts `b` from `a`
  GET      `/api/v1/multiply?a=2&b=3`   Multiplies `a` and `b`
  GET      `/api/v1/divide?a=6&b=3`     Divides `a` by `b`

### Example request

``` bash
curl -i "http://localhost:8080/api/v1/add?a=2&b=3"
```

Example successful response (`200 OK`):

``` json
{
  "operation": "add",
  "a": 2,
  "b": 3,
  "result": 5
}
```

The response contains the operation name, both operands, and the result.
Numeric values are represented using `BigDecimal`.

### Error responses

The API returns a structured JSON error body with these fields:

``` json
{
  "timestamp": "2026-10-26T19:04:57Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Division by zero is not allowed",
  "path": "/api/v1/divide"
}
```

The timestamp above is an example; the actual value is generated when
the error occurs.

Common client errors return **`400 Bad Request`**.

**Division by zero**

``` bash
curl -i "http://localhost:8080/api/v1/divide?a=6&b=0"
```

The message is `Division by zero is not allowed`.

**Missing parameter**

``` bash
curl -i "http://localhost:8080/api/v1/add?a=2"
```

The message identifies the missing parameter, for example
`Missing required parameter: b`.

**Invalid numeric parameter**

``` bash
curl -i "http://localhost:8080/api/v1/add?a=abc&b=2"
```

The message identifies the parameter that must be a valid number.

Unexpected server errors are logged by the application and return
**`500 Internal Server Error`** with the generic message
`An unexpected error occurred`.

## Health check

Spring Boot Actuator exposes the health endpoint at:

``` bash
curl -i "http://localhost:8080/actuator/health"
```

A healthy running application returns `200 OK` with a JSON status of
`UP`.

## Tests

The project includes:

-   **`CalculatorServiceTest`** --- unit tests for addition,
    subtraction, multiplication, division, and division by zero.
-   **`CalculatorControllerTest`** --- MockMvc integration tests for a
    successful request, division by zero, missing parameters, and
    non-numeric parameters.
-   **`MiniProjectSpeApplicationTests`** --- verifies that the Spring
    application context loads.

Run all tests with:

``` bash
./mvnw test
```

## Project structure

``` text
src/
├── main/
│   ├── java/com/harshsinha/miniprojectspe/
│   │   ├── controller/
│   │   │   └── CalculatorController.java
│   │   ├── dto/
│   │   │   ├── ApiErrorResponse.java
│   │   │   └── CalculatorResponse.java
│   │   ├── exception/
│   │   │   ├── DivisionByZeroException.java
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── service/
│   │   │   └── CalculatorService.java
│   │   └── MiniProjectSpeApplication.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/harshsinha/miniprojectspe/
        ├── controller/
        │   └── CalculatorControllerTest.java
        ├── service/
        │   └── CalculatorServiceTest.java
        └── MiniProjectSpeApplicationTests.java
```

`target/` contains generated build output and test reports; it is not
part of the source tree.
