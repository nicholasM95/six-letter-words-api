# Six Letter Words Api

## Build docker
```bash
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=six-letter-words-api:1.0.0 -pl application
```

## Start docker
```bash
docker run -p 8080:8080 six-letter-words-api:1.0.0
```

## Start docker from public repository
```bash
docker run -p 8080:8080 ghcr.io/nicholasm95/six-letter-words-api:1.0.0
```
