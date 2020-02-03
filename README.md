# offer-service
A RESTful application.

## Assumptions
There is no authentication but some degree of authorization was necessary.

## Prerequisites
The project can run in any Java 1.8+ environment but for convenience a Docker image has been included.

## Compiling and Running
### Using Docker
The project can be run effortlessly in a Docker container using the provided script `build.sh`. To build the container,
type:
```
$ chmod +x build.sh
$ sh build.sh
```
The script builds a Docker image named `offer-service`. To run the image, type:
```
$ docker run --rm -p 8080:8080 offer-service:latest
```

### Using Gradle
You can compile and run the project with Gradle typing:
```
chmod +x gradlew && sh gradlew build && java -jar build/libs/offer-0.0.1-SNAPSHOT.jar
```

## Documentation
Once the server is running, an OpenAPI Specification can be found at
[http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs) or in Swagger UI at
[http://localhost:8080/swagger-ui.html#/](http://localhost:8080/swagger-ui.html#/).

## API
The following table summarizes the implemented API.

| API                | Endpoint                                    | Request body | Response body  |
|--------------------|---------------------------------------------|--------------|----------------|
| List of users      | `GET /users`                                |              | List<UserDto>  |
| User details       | `GET /users/{userId}`                       |              | UserDto        |
| Offers by user     | `GET /users/{userId}/offers`                |              | List<OfferDto> |
| List of offers     | `GET /offers`                               |              | List<OfferDto> |
| Offer details      | `GET /offers/{offerId}`                     |              | OfferDto       |
| Create a new offer | `POST /offers?user.id={userId}`             | OfferRequest |                |
| Modify an offer    | `PUT /offers/{offerId}?user.id={userId}`    | OfferRequest |                |
| Delete an offer    | `DELETE /offers/{offerId}?user.id={userId}` |              |                |

## Tools and Libraries
- Spring Initializr
- Spring Boot
- Spring MVC
- Project Lombok
- Spring Data JPA
- JPA Static Metamodel Generator
- Criteria Queries
- Hibernate
- ModelMapper
- Spring Security
- H2 Database Engine
- SpringDoc Open API
- Gradle
- Git
- Mockito
