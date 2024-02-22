# Six Letter Words Api

## Run locally
This project includes a pre-configured run setup for IntelliJ IDEA. 
To start the local development environment, Docker Compose must be available, and Docker must be running on your machine.

## Test endpoints
```bash
curl http://localhost:8080/word --form file=@"word.txt"
```
```bash
curl http://localhost:8080/word
```
```bash
curl http://localhost:8080/word/{id}
```


## Using the OpenAPI Specification
This project utilizes the OpenAPI Specification to define and document the API endpoints. 
You can import this [specification](https://github.com/nicholasM95/six-letter-words-api/blob/main/word-adapter/src/main/resources/word-openapi-spec.yaml) into tools like Postman for easy testing and interaction with the API.
