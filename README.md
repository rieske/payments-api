# payments-api

REST API for storing, updating and retrieving Payment resources

### Prerequisites

- JDK11
- docker `18.09.5` or later
- docker-compose `1.23.2` or later


### Building

`./gradlew build`

### Generating documentation

After the project is built:

`./gradlew asciidoc`

And the API documentation will be made available at `build/docs/html5/index.html`

### Running

`docker-compose up --build`

The HAL browser can be accessed at: http://localhost:8080/api/v1/browser


### Implementation

Frameworks utilized:
- `spring boot 2` - for bootstrapping
- `spring-data-rest` - for exposing JPA entities via a RESTful API
- `hibernate` - ORM
- `flyway` - DB schema versioning
- `pact` - consumer driven contract tests
- `cucumber` - BDD
- `spring-restdocs` - test-driven documentation

### Shortcomings

The schema endpoint does not return a correct schema in a sense that it does not have any validation information
in it. This is apparently what the framework does not provide at this point.

