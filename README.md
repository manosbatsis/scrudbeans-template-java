# ScrudBeans Project Template for Java [![Build Status](https://travis-ci.com/manosbatsis/scrudbeans-template-java.svg?branch=master)](https://travis-ci.com/manosbatsis/scrudbeans-template-java)

Sample project template for a [ScrudBeans](https://github.com/manosbatsis/scrudbeans) project.
Demonstrates ScrudBeans generated component classes like repositories, services and controllers, as 
well as custom/explicit components. Both Maven and Gradle are supported. 

The following sections will guide you in building and testing the project. 

**Prerequisites**: JDK 1.8+


## Clone and Build

Clone:

```bash
git clone https://github.com/manosbatsis/scrudbeans-template-java.git
```

Navigate to the project directory

```bash
cd scrudbeans-template
```

Build and execute [integration tests](src/test/) 
using Maven:

```bash
./mvnw clean verify install 
```

or, if you prefer Gradle:

```bash
./gradlew clean build integrationTest
```

## Manual Test

To launch the app with Maven:

```bash
./mvnw spring-boot:run
```

or Gradle:

```bash
./gradlew bootRun
```

![./mvnw spring-boot:run][spring-boot-run]


You can now browse the Swagger UI documentation at http://localhost:8080/swagger-ui.html

![swagger-ui][swagger-ui]


## RSQL Test

RSQL is a query language for parametrized filtering of entries in RESTful APIs. The project endpoints support RSQL 
by conversion to JPA criteria. For eample, to search for _orders between two timestamps_ (i.e. Dec 10th, 2018), one would use a URL 
like http://localhost:37059/api/rest/orders?filter=createdDate%3Dge%3D2018-12-10T00%3A00%3BcreatedDate%3Dle%3D2018-12-10T23%3A59%3A59.999999999


![RSQL Example][rsql-eample]

[spring-boot-run]: etc/img/spring-boot-run.png "Manually launching app"
[swagger-ui]: etc/img/swagger-ui.png "Swagger UI"
[rsql-eample]: etc/img/rsql-eample.png "RSQL example: search between dates"

