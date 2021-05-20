## Coin Calculator
A service that can show btc prices and enables buying of BTC with coin amount or currency amount.

The customer first applies for a buying amount and receives a unit price and reservation id. 
If the price is not expired, he/she can buy the amount he has reserved by via reservation id.   
### Run
The service runs on localhost 8080 port.
It exposes a Swagger UI on http://localhost:8080/swagger-ui.html#/

### Use

The service exposes three endpoints

**/prices** : Lists the current prices of BTC

**/reserve** : Starts the process of buying coins. Returns a reservation id, unit price and total price of the trade.

**/buy** : Buys the amount of coins reserved. Returns HTTP 400 if the price is expired.