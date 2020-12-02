## Requirements

Create generator of random people that keeps statistical properties of population.
Service can receive samples of people and update its statistics accordingly.
Based on known statistics service can generate batches of random people of given size.
Person has family name, given name, sex, birth date and zip code.
Implement HTTP API for the service, use JSON as data format.

Use following technologies:
 - scala
 - postgres
 - akka-http

## Tools used for development

IntelliJ IDEA Ultimate

Postman for testing the requests

## External libraries used

Slick for the database mapping:
source: https://scala-slick.org/

Flyway for database migration
source: https://flywaydb.org/



Full list of dependencies from the *sbt.build* file:

```scala
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
      "ch.qos.logback"    % "logback-classic"           % "1.2.3",
      "com.typesafe.slick" %% "slick" % "3.3.3",
      "org.postgresql" % "postgresql" % "42.2.18",
      "org.flywaydb" % "flyway-core" % "7.3.0",
      "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"                % "3.0.8"         % Test
    )
```



## About the delivered work

I composed this project during the past two weekends I roughly spend spend 12 to 15 hours in total to make this. 

I had no prior experience with Scala, Akka-HTTP and minimal knowledge of working with PostgreSQL. During development I opted to make use of Slick for the relational mapping in conjunction with Flyway for migration simply because I saw it used a lot in example rest API's made in Akka-HTTP during research so it seemed convenient for me to use. Note the that the queries to the database themselves are done in a more programmatic way opposed of traditional SQL queries but the idea remains the same.

During development I have tested the application by running it in the terminal inside my IDE and throughout Postman to do the actual requests to the API. I have taken no additional measures in terms of creating a build script or implementation of visual interface, I hope that is ok. 

Since there was some ambiguity in terms of the statistical properties, I have chosen to keep it very simple and minimal and I'm  sure I have overlooked things.

Currently the application is able to generate n amount of people with some additional given parameters:(total People, Female Percentage, Male Percentage and from year to year). The names are being read from three text files, last_names.txt, male_first_names.txt and female_first_names.txt, I included these files in a separate folder inside the resources folder of the application. The random birth dates and zip codes are randomly generated as String format in the application.



I went trough the following steps:

1. Get to grips with the basics of Scala and implemented the code to randomly generate people.
2. Implemented a basic HTTP server with Akka and performed basic Get & Post requests and used local storage to store the data.
3. Implemented code to connect and write to the database, further refined the API, clean the code and wrote documentation.

To develop this project.

## Usage 

It is recommend to try out the application inside the IDE or from the command line and use a tool like Postman or curl command to test the API.

Also make sure that a database named people exists on your local machine, there is no need to create the schema this will automatically be generated by Flyway if the the schema is not present in the database. 

Alternatively you could use the people.sql file I included inside the project inside the backup-db folder to restore the database from the *PostgreSQL\13\bin*  directory with the following command:

```
psql -U postgres -d people -1 -f <path to file>
```

If you take the second approach make sure to comment out the `reloadSchema()`  and uncomment the `migrate()` code line inside the App.scala file.

```scala

   // If this fails uncomment the flyway.repair() line in the migrate method in the 		migrationConfig file
    migrate()

    // I have chosen to reset the database everytime the application runs,
    // uncomment the previous line if this behaviour is not decided
    // reloadSchema()
```



The database has the following configuration:

```
 db {
  url = "jdbc:postgresql://localhost:5432/people"
  user = "postgres"
  password = "1234"
  }
```

If you match this on your local system or change the configuration inside `/resources/application.conf` everything should work accordingly.

The network settings in `application.conf` are the following:

```
  http {
  interface = "localhost"
  port = 8080
  }
```



## Database schema

The database is very simple and only contains a single table called person to store the person objects:

```sql
CREATE TABLE IF NOT EXISTS "person"
(
    "id"        Int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    "lastname"  VARCHAR(100) NOT NULL,
    "firstname" VARCHAR(100) NOT NULL,
    "sex"       Char(1)      NOT NULL,
    "birthdate" VARCHAR(10)  NOT NULL,
    "zipcode"   Char(6)      NOT NULL

);
```

I initially used a Date format with the birthdate. However, this caused some issues during development so I opted to use String format instead.

## API description

### Person routes:

*Post:*

```
http://localhost:8080/api/people
```

You can post a new single person to the database by entering the following things in the request body.

```
{
"lastname": "Doe", 
"firstname": "John", 
"sex": "M", 
"birthdate": "1990-05-17", 
"zipcode": "6781GM"
}
```

should return Status: 201 created

*Put:*

```
http://localhost:8080/api/person?id=5
```

Should return Status: 201 created

*Delete:*

```
http://localhost:8080/api/person?id=5
```

Should give the result as Status: 200 OK



### People routes: 

*Get:*

```
http://localhost:8080/api/people
```

Returns all people in the database 

Should return Status : 200 OK

*Post:*

```
http://localhost:8080/api/people
```

Including the following parameters inside the request body in JSON format

```
{
"totalPeople": 4, 
"femalePercent": 50, 
"malePercent": 50, 
"from": 1900, 
"to": 2000
}
```

Yields a result such as:

Status: 201 created

If we would then do a get request this would be the output in postman:

```
[
    {
        "birthdate": "1908-03-12",
        "firstname": "Karlyn",
        "id": 1,
        "lastname": "Camelin",
        "sex": "F",
        "zipcode": "6067UA"
    },
    {
        "birthdate": "1980-11-25",
        "firstname": "Marvis",
        "id": 2,
        "lastname": "Canty",
        "sex": "F",
        "zipcode": "5796HX"
    },
    {
        "birthdate": "1933-03-15",
        "firstname": "Lemuel",
        "id": 3,
        "lastname": "Curcher",
        "sex": "M",
        "zipcode": "5913PK"
    },
    {
        "birthdate": "1991-10-30",
        "firstname": "Loren",
        "id": 4,
        "lastname": "Sapey",
        "sex": "M",
        "zipcode": "9298HB"
    }
]
```

*Delete:*

```
http://localhost:8080/api/people
```



### Search routes:

Allows the following filter functionalities.

*Get* request to return a single person with specified id.

```
http://localhost:8080/api/search?id=7
```

Should return Status: 302 Found



*Get* request to return all people with specified sex.

```
http://localhost:8080/api/search?sex=F
```

Should return Status: 302 Found



*Get* request to return all people from a specific year.

```
http://localhost:8080/api/search?year=1990
```

Should return Status: 302 Found



*Get* request to return all people between start and end id range with option to further filter for gender.

```
http://localhost:8080/api/search/range?start=1&end=10
```

Or with optional gender option:

```
http://localhost:8080/api/search/range?start=5&end=15&sex=M
```

Should return Status: 302 Found



*Get* request to return all people between from year and to year with option to further filter for gender. 

```
http://localhost:8080/api/search/between?from=1950&to=2000
```

Or with optional gender option:

```
http://localhost:8080/api/search/between?from=1950&to=2000&sex=F
```

Should return Status: 302 Found



*Get* request that returns a random batch of people of specified size with option to further filter for gender. 

```
http://localhost:8080/api/search/random?amount=5
```

Or with optional gender option:

```
http://localhost:8080/api/search/random?amount=5&sex=F
```

Should return Status: 302 Found



## Potential further improvements

I'm sure that many things could be done better regarding the best practices in coding in Scala and working with Akka HTTP.  But due to my lack of familiarity with the language and functional programming in general its hard to make a good judgement on that myself. Regardless, I can think of a few improvements myself I will list the below:

- More filtering functionality, currently speaking its quite limited and it only covers a few cases.
- Better error handling, I tried my best to prevent the most common things that might break the application but I'm sure there are still plenty of ways to cause errors. One example of this would be to build in a custom exception that handles certain cases, right now the feedback from the API itself is very generic and limited.
- Even though the application is able to write and request a large amount of people, it gets quite slow when doing large requests to the database, I'm sure optimizations could be made to improve this.
- Tests I did not write any unit tests and did not really allowed myself to take the time to get familiar with the testing framework that's included inside the project.