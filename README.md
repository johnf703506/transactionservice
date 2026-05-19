
## Running Locally

- Run `mvn clean install`
- Open Application.java class and run that class,  or run `java -jar target/transaction-service-1.0.0.jar` from the terminal.
- Go to http://localhost:8080/swagger-ui.html to test.

## Running Locally in Docker

- Run `mvn clean install`
- Run `docker build -t transaction-service .`
- Run `docker run -d -p 8080:8080 --name transaction-service transaction-service`
- Go to http://localhost:8080/swagger-ui.html to test
- When done, run `docker stop transaction-service` to stop the container

## Other Notes

- The treasury data is loaded the first time the application is run.  I added an endpoint to refresh the currency rates.  For a production system I would create a CRON job that would updated these daily.  I would also cache this data instead of calling the database each REST call.
- I did not put any security on the endpoints which would be different 
- If running in docker, the data is persisted if the container is stopped and restarted.  I did not mount anything to the container so it would be persisted outside the container to handle the situatuon where the container is deleted.  
- H2 DB console is available at http://localhost:8080/h2-console when running locally, user sa, empty password.
- Databases are created on the fly but normally these would be scripted with something like Flyway.


