# spring-boot-jersey-jwt-auth
Simple playground application using Spring Boot, Jersey and JWT to
* secure a resource
* provide an endpoint to supply JWT access tokens to the resource

## Running the sample app
```
java -jar -Dspring.profiles.active=production target/spring-boot-jersey-jwt-auth-0.0.1-SNAPSHOT.jar
```

### HTTPS
```
curl -v -k -H "Content-Type: application/json" -H "Accept: application/json" -X POST -d '{"username":"user","password":"password"}' 'https://localhost:8443/auth/login'
```

```
{
  "token" : "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNDUzMTA5NDQ5fQ.o88IDKmssJpqabJM0ifqG714F8VPph77Ag7WP5ISTqVeyUurWJ1S4TZFWK9TdPTiTrM4x4dsTZGGq7MQjdDo2Q"
}
```

```
curl -v -k -X GET -H "X-Auth-Token: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNDUyODQ2MDMzfQ.REPyi3v43KJ2eMbHspSY61RurNgDnI4G3ZrjQR3f6vJ3BvSBPBrj0KjTscndXo6UwAJK14pAgtDU0EWzVih1xA" -H "Cache-Control: no-cache" -H "Postman-Token: a6417c2c-90a5-670f-8a3d-309063e86b0c" 'https://localhost:8443/hello?name=jdoe'
```

```
{
  "greeting" : "Hello jdoe"
}
```

### Redirect HTTP to HTTPS (optional)

```
curl -v -k -L -H "Content-Type: application/json" -H "Accept: application/json" -X POST --post302 -d '{"username":"user","password":"password"}' 'http://localhost:8080/auth/login'
```

```
{
  "token" : "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNDUzMTA5NDQ5fQ.o88IDKmssJpqabJM0ifqG714F8VPph77Ag7WP5ISTqVeyUurWJ1S4TZFWK9TdPTiTrM4x4dsTZGGq7MQjdDo2Q"
}
```

```
curl -v -k -L -X GET -H "X-Auth-Token: eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNDUyODQ2MDMzfQ.REPyi3v43KJ2eMbHspSY61RurNgDnI4G3ZrjQR3f6vJ3BvSBPBrj0KjTscndXo6UwAJK14pAgtDU0EWzVih1xA" -H "Cache-Control: no-cache" 'http://localhost:8080/hello?name=jdoe'
```

```
{
  "greeting" : "Hello jdoe"
}
```
## Credits
Heavily inspired by [brahalla/Cerberus](https://github.com/brahalla/Cerberus) 
