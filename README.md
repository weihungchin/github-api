# Getting Started

### Build the project
At project root directory, run `./gradlew clean build`

### To start the project locally
At project root directory, run `./gradlew bootRun`

Then, send a GET request to: `localhost:8080/api/repos/contributors` to see the results.

### Description
This project is built with Kotlin Spring Boot and Gradle build tool. It is intended as a showcase and was completed within 3 hours.

#### To do/Potential improvement:
- Add swagger docs
- Add SpringBootTest for integration test
- Add wiremock
- More request validation and exception handling
- Replace mokitto with mockk
- Add logging
- Replace restTemplate with Spring Cloud Feign client
