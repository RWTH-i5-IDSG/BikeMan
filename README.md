# BikeMan

BikeMan is a Bike Sharing Scheme (http://en.wikipedia.org/wiki/Bicycle_sharing_system) <b>Man</b>agment Software, especially for e-bikes/pedelecs. Core technologies are [Spring Boot](http://projects.spring.io/spring-boot/) and [AngularJS](https://angularjs.org/). With BikeMan it is possible to manage Bike Stations, (e)bikes, fleetmanagers, and customers. The *PSInterface* offers an API to Stations which implements this specifications (specifications will follow).

This application is generated with [JHipster](http://jhipster.github.io/) 8.0 with OAuth2.0 Authentication and PostgreSQL (as dev and prod db).

## Requirements

To build BikeMan you need at least the latest version of Maven, PostgreSQL, and Java JDK 7.

## Configuration

Database tablename and user is by default `bikeman` (no password required). 

## Start BikeMan

With `mvn spring-boot:run` the application will start with an embedded Tomcat and port *8080* and default users:

* Admin (`admin@bikeman.com` `password: admin`)
* User (`user@bikeman.com` `password: user`)

For more information about technology stack and using in production take a look at the JHipster Documentation.

## Demo Station for Testing Purpose

BikeMan can be tested with the [Bikesharing-Station-Demo Application](https://github.com/RWTH-i5-IDSG/Bikesharing-Station-Demo).
