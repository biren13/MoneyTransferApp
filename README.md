# RevolutMoneyTransferApp - A REST API for to transfer  money between accounts <br> <br>
### Requirements
> Java 9+
### Build - Run
1.In Project Root Directory,type <br/>
```$xslt
 mvn clean package 
 java -jar target/RevolutMoneyTransferApp-1.0-SNAPSHOT-jar-with-dependencies.jar 
(App will be running on http://localhost:4567)
```
## Application usage
### Create Money Transfer Transaction
The following request creates an account and returns empty body with 201 status code
```
    POST http://localhost:4567/Transfer
    { 
        "fromAccountId":1,
        "toAccountId":2,
        "amount":10,
        "currency":"USD"
    }
```
Response:
```
    HTTP 200 OK
    {"status":"SUCCESS","message":"successful"}
```