BikeMan - Bike Manager
==========================

BikeMan is a management and backend system for pedelec rental stations.

### System Requirements

* JDK 8
* Maven
* Postgres 9.5 or 9.6

It is designed to run as standalone, so a java servlet container / web server (e.g. Apache Tomcat), is **not** required.

### Configuration and Installation

1. Database preparation:

    Make sure that a database user "[BikeMan](src/main/resources/config/application-prod.yml#L12)" and a database "[BikeMan](src/main/resources/config/application-prod.yml#L10)" exist (or change these however you want). The user should be the owner of the database.
    
    The database migrations will be done by [Liquibase](https://www.liquibase.org/) when the application starts.
    
    
2. Start the application with

    ```
    mvn spring-boot:run
    ```
    
3. Access the web interface at `http://localhost:8080/`. The initial credentials are defined in [users.csv](src/main/resources/config/liquibase/users.csv). So, you can login with the email `admin@bikeman.com` and password `admin`. Change these in a production system!
