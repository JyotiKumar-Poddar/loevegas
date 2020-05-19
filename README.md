# LeoVegas Springboot app

Create API for wallet that provides below service

- Debit Transaction
- Credit Transaction
- Transaction History
- Account balance 


## Requirements

- [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Gradle](https://gradle.org/)

## Getting Started

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `VegasApplication.class` class from your IDE.

Alternatively you can use the [Spring Boot Gradle plugin](https://docs.spring.io/spring-boot/docs/current/gradle-plugin/reference/html/) like so:

```shell
gradle bootRun
```

# REST API

application have default user that is user1@gmail.com and user id id mandatory. 

## Posting request using curl

### Request

`POST /debit/`

    curl -X POST  http://localhost:8080/credit -H 'cache-control: no-cache' -H 'content-type: application/json' -d '{ "amount": 100, "userId": "user1@gmail.com", "transactionId": "1823wsa70" }'

### Response

    {"transactionId":"1823wsa7099","transactionAmount":100,"transactionStatus":"COMPLETED","transactionType":"CREDIT"}
