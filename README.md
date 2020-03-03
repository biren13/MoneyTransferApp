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
#### Get Account Transaction 
```
    GET http://localhost:4567/Transfer/1
  ```
Response:
```
    HTTP 200 OK
    {"status":"SUCCESS","data":[{"id":1,"accountId":1,"creditDebitIndicator":"D","amount":10,"currency":"USD","baseAmount":10,"baseCurrency":"USD","description":"Transfer money","valueDate":"Mar 2, 2020, 8:21:00 PM"}]}
   
```