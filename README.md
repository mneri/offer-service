# offer-service
A RESTful application.

## Assumptions
It is assumed that the service lives behind a gateway that provides authentication. The gateway will redirect requests
and add a `?user.id={userId}` query string. The query string will be used for authorization.

## Prerequisites
The project can run in any Java 1.8+ environment but for convenience a Docker image has been included.

## Compiling and Running
### Using Docker
The project can be run effortlessly in a Docker container using the provided script `build.sh`. To build the container,
type:
```
$ chmod +x build.sh && sh build.sh
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
### Online documentation
Once the service is up and running, an in depth documentation can be found at
[http://localhost:8080/swagger-ui.html#/](http://localhost:8080/swagger-ui.html#/); the OpenAPI specification can be
found at [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs).


### Schema
The API revolves around three different messages `UserDto`, `OfferDto`, `OfferRequest`, shown below.

```
UserDto {
    id: string
    username: string
}
```

```
OfferDto {
    id: string
    title: string
    description: string
    price: number
    currency: string
    createTime: date
    ttl: integer
}
```

```
OfferRequest {
    title: string
    description: string
    price: number
    currency: string
    ttl: integer
}
```

### API
The following table summarizes the implemented API:

| API                | Endpoint                                    | Request body   | Response body    |
|--------------------|---------------------------------------------|----------------|------------------|
| List of users      | `GET /users`                                |                | `List<UserDto>`  |
| User details       | `GET /users/{userId}`                       |                | `UserDto`        |
| Offers by user     | `GET /users/{userId}/offers`                |                | `List<OfferDto>` |
| List of offers     | `GET /offers`                               |                | `List<OfferDto>` |
| Offer details      | `GET /offers/{offerId}`                     |                | `OfferDto`       |
| Create a new offer | `POST /offers?user.id={userId}`             | `OfferRequest` |                  |
| Modify an offer    | `PUT /offers/{offerId}?user.id={userId}`    | `OfferRequest` |                  |
| Delete an offer    | `DELETE /offers/{offerId}?user.id={userId}` |                |                  |

## Tools and Libraries
- [Criteria Queries](https://docs.jboss.org/hibernate/entitymanager/3.5/reference/en/html/querycriteria.html)
- [Git](https://git-scm.com/)
- [Gradle](https://gradle.org/)
- [H2 Database Engine](https://www.h2database.com/html/main.html)
- [Hibernate](https://hibernate.org/)
- [JPA Static Metamodel Generator](https://docs.jboss.org/hibernate/orm/5.0/topical/html/metamodelgen/MetamodelGenerator.html)
- [Mockito](https://site.mockito.org/)
- [ModelMapper](http://modelmapper.org/)
- [Project Lombok](https://projectlombok.org/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Initializr](https://start.spring.io/)
- [Spring MVC](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html)
- [Spring Security](https://spring.io/projects/spring-security)
- [SpringDoc Open API](https://springdoc.github.io/springdoc-openapi-demos/)
