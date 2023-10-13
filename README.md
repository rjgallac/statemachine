# Spring state machine

https://spring.io/projects/spring-statemachine

## access h2 here

http://localhost:8080/h2-console


## How to use

create initial application by running following

```
curl -X POST http://localhost:8080/application -H "Content-Type: application/json" -d '{}'
```

step throw workflow by executing following
```
curl -X PUT http://localhost:8080/application/1/SUBMIT -H "Content-Type: application/json" -d '{}'
curl -X PUT http://localhost:8080/application/1/REVIEWED -H "Content-Type: application/json" -d '{}'
curl -X PUT http://localhost:8080/application/1/APPROVE -H "Content-Type: application/json" -d '{}'
curl -X PUT http://localhost:8080/application/1/CLOSE -H "Content-Type: application/json" -d '{}'
```

create second app and step through

```text
curl -X POST http://localhost:8080/application -H "Content-Type: application/json" -d '{}'
curl -X PUT http://localhost:8080/application/2/SUBMIT -H "Content-Type: application/json" -d '{}'
curl -X PUT http://localhost:8080/application/2/REVIEWED -H "Content-Type: application/json" -d '{}'
```

you wont be able to jump around states e.g.
```text
curl -X POST http://localhost:8080/application -H "Content-Type: application/json" -d '{}'
curl -X PUT http://localhost:8080/application/2/SUBMIT -H "Content-Type: application/json" -d '{}'
curl -X PUT http://localhost:8080/application/1/CLOSE -H "Content-Type: application/json" -d '{}'


```


