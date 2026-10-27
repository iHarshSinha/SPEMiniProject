# Spring Boot Calculator API

A RESTful calculator API built with Spring Boot 4 and Java 21. It supports addition, subtraction, multiplication, and division using decimal numbers. The API returns JSON responses and provides centralized error handling for invalid requests.

The project also includes Docker containerization, Nginx reverse proxy configuration, Docker Compose orchestration, and a Jenkins CI/CD pipeline.

## Technologies

- Java 21
- Spring Boot 4.0.9
- Maven
- Spring MVC
- Spring Boot Actuator
- JUnit 5 and MockMvc
- Docker and Docker Compose
- Nginx
- Jenkins
- Docker Hub

## Prerequisites

For local development:

- Java 21 or later
- Maven, or the included Maven Wrapper (`./mvnw`)

For containerized deployment:

- Docker Desktop or Docker Engine
- Docker Compose
- Internet access to pull the required Docker images

Check your Java installation:

```bash
java -version
```

## Build and test

From the project root, run the tests:

```bash
./mvnw test
```

Build and verify the project, including tests:

```bash
./mvnw clean verify
```

To package the executable JAR:

```bash
./mvnw clean package
```

Maven test reports are generated in `target/surefire-reports/`, including XML reports suitable for test-reporting tools.

## Run the application locally

Start the application using Maven:

```bash
./mvnw spring-boot:run
```

Alternatively, build the JAR and run it directly:

```bash
java -jar target/MiniProjectSPE-0.0.1-SNAPSHOT.jar
```

When running Spring Boot directly, the default application port is `8080`.

The application is available at:

`http://localhost:8080`

## Calculator API

All calculator endpoints use **HTTP GET** and require two query parameters: `a` and `b`. Both parameters must be valid numbers.

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/add?a=2&b=3` | Adds `a` and `b` |
| GET | `/api/v1/subtract?a=5&b=3` | Subtracts `b` from `a` |
| GET | `/api/v1/multiply?a=2&b=3` | Multiplies `a` and `b` |
| GET | `/api/v1/divide?a=6&b=3` | Divides `a` by `b` |

### Example request

```bash
curl -i "http://localhost:8081/api/v1/add?a=2&b=3"
```

Example successful response (`200 OK`):

```json
{
  "operation": "add",
  "a": 2,
  "b": 3,
  "result": 5
}
```

The response contains the operation name, both operands, and the result. Numeric values are represented using `BigDecimal`.

### Error responses

The API returns a structured JSON error body with these fields:

```json
{
  "timestamp": "2026-10-26T19:04:57Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Division by zero is not allowed",
  "path": "/api/v1/divide"
}
```

The timestamp above is an example; the actual value is generated when the error occurs.

Common client errors return **`400 Bad Request`**.

**Division by zero**

```bash
curl -i "http://localhost:8081/api/v1/divide?a=6&b=0"
```

The message is `Division by zero is not allowed`.

**Missing parameter**

```bash
curl -i "http://localhost:8081/api/v1/add?a=2"
```

The message identifies the missing parameter, for example `Missing required parameter: b`.

**Invalid numeric parameter**

```bash
curl -i "http://localhost:8081/api/v1/add?a=abc&b=2"
```

The message identifies the parameter that must be a valid number.

Unexpected server errors are logged by the application and return **`500 Internal Server Error`** with the generic message `An unexpected error occurred`.

## Health check

Spring Boot Actuator exposes the health endpoint at:

```bash
curl -i "http://localhost:8081/actuator/health"
```

A healthy running application returns `200 OK` with a JSON status of `UP`.

## Docker

The project includes a multi-stage Dockerfile that builds the application using Maven and Java 21, then copies the executable JAR into a smaller Java runtime image.

Build the Docker image from the project root:

```bash
docker build -t calculator-api .
```

Run the calculator container:

```bash
docker run --rm -p 8080:8080 calculator-api
```

The application is then available at `http://localhost:8080`.

The Dockerfile's `EXPOSE 8080` instruction documents the expected application port inside the container. It does not publish that port on the host by itself.

## Docker Compose and Nginx

The project includes a `docker-compose.yml` file that defines two services:

- **Calculator:** Runs the Spring Boot application using the published Docker Hub image.
- **Nginx:** Acts as a reverse proxy and forwards incoming requests to the calculator service.

Both services are connected through a Docker bridge network named `calculator-network`.

### Port configuration

The containerized deployment uses the following ports:

| Port | Purpose |
|---|---|
| `8081` | Host port used to access the application through Nginx |
| `80` | Port on which Nginx listens inside its container |
| `8080` | Port on which Spring Boot listens inside the calculator container |

The request flow is:

`localhost:8081` → Nginx port `80` → calculator port `8080`

This means the full Docker Compose deployment is accessed through **port `8081`**, while direct local Spring Boot execution uses port `8080`.

### Start the complete deployment

From the project root:

```bash
docker compose up -d
```

By default, Compose uses the `latest` calculator image unless `IMAGE_TAG` is provided through the environment.

To inspect the running services:

```bash
docker compose ps
```

To view logs:

```bash
docker compose logs -f
```

To stop the deployment:

```bash
docker compose down
```

### Access the API through Nginx

Once the services are healthy, use port `8081`:

```bash
curl -i "http://localhost:8081/api/v1/add?a=2&b=3"
```

Check application health through the same route:

```bash
curl -i "http://localhost:8081/actuator/health"
```

Nginx forwards these requests to the calculator container over the internal Docker network.

### Health checks and service startup

The calculator service has a Docker health check that requests `/actuator/health` on port `8080` inside the calculator container.

Nginx is configured with a dependency on the calculator's healthy state, so Compose waits for the calculator to become healthy before starting Nginx.

The Compose configuration also uses a restart policy to help keep the services running.

### Running only the calculator

The Dockerfile can be used independently of Docker Compose. Build and run the calculator image directly if you do not need Nginx:

```bash
docker build -t calculator-api .
docker run --rm -p 8080:8080 calculator-api
```

This starts only the Spring Boot application, not the Nginx service.

## Jenkins CI/CD pipeline

The project uses Jenkins to automate building, testing, publishing, and deploying the application.

The pipeline follows this sequence:

1. **GitHub webhook:** A push to the repository triggers the Jenkins job.
2. **Build:** Jenkins uses Maven to package the Spring Boot application.
3. **Test:** Jenkins runs the test suite and publishes the JUnit XML reports.
4. **Docker Build:** Jenkins reads the Dockerfile and builds the calculator image.
5. **Docker Push:** Jenkins authenticates to Docker Hub using credentials stored in Jenkins and pushes both a build-number tag and the `latest` tag.
6. **Deploy:** Jenkins exports the rent build number as `IMAGE_TAG`, pulls the corresponding image through Docker Compose, and starts or updates the services.
7. **Health Check:** Jenkins checks the application through Nginx at `http://localhost:8081/actuator/health`.
8. **Email Notification:** Jenkins sends a notification containing the build status and console output URL.

### Image versioning

Images are tagged with the Jenkins build number as well as `latest`.

For example, build number `42` produces:

```text
iharshsinha/calculator-api:42
iharshsinha/calculator-api:latest
```

The deployment uses the numbered tag for that build, allowing a specific image version to be selected instead of relying on the moving `latest` tag.

### Docker Hub and deployment

The Docker image is published to Docker Hub at:

`iharshsinha/calculator-api`

The Docker Compose configuration pulls the image from Docker Hub rather than building the calculator image locally. Nginx is pulled separately from the `nginx:alpine` image.

This separation allows the same published application image to be deployed on another machine without compiling the source code on that machine.

### Jenkins and ngrok

For local Jenkins development, ngrok can expose the Jenkins server to GitHub so that webhook requests can reach it.

With a randomly assigned ngrok URL, restarting the tunnel may change the public URL. The GitHub webhook must then be updated unless a stable ngrok domain is configured. The tunnel must remain running for GitHub to reach the local Jenkins instance.

Docker Hub credentials should be configured in Jenkins Credentials rather than committed to the repository.

## Tests

The project includes:

- **`CalculatorServiceTest`** — unit tests for addition, subtraction, multiplication, division, and division by zero.
- **`CalculatorControllerTest`** — MockMvc integration tests for a successful request, division by zero, missing parameters, and non-numeric parameters.
- **`MiniProjectSpeApplicationTests`** — verifies that the Spring application context loads.

Run all tests with:

```bash
./mvnw test
```

## Project structure

```text
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

Additional deployment files at the project root include:

```text
Dockerfile
.dockerignore
docker-compose.yml
Jenkinsfile
nginx/
└── default.conf
```

`target/` contains generated build output and test reports; it is not part of the source tree.
