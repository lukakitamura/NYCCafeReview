# Java based NYC cafe review application and database
### At the moment, the webpage is hosted on localhost port 8080
#### TO RUN: 
1. In the top level of this directory, i.e. where this README is located, run ```mvn clean package```
2. Once the build is completed run ```java -jar target/cafe-review-1.0-SNAPSHOT-jar-with-dependencies.jar```

#### Tools used: 
* All dependecies will be built through running the maven file (pom.xml): 
- Java 22
- Maven 4.0.0
Backend: 
- Embedded Apache Tomcat 10.1.19
    - Jakarta servlet
    - Concurrent user management/thread safety (Although this is not used as it is running locally)
- SQLite
- Bcrypt for password hashing

Frontend: 
- Glassfish for Jakarta Server Pages
    - HTML and CSS 
- OpenStreetMap API 
-
