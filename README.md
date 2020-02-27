
This repository is based on Akka HTTP & it's Ruting DSL.
Goal is to provide backend API for Ethereum blockchain parsed data (mainly blocks & transactions)

Currently these endpoints are functional. Also screenshots attached in /screenshots directory.

http://localhost:8080/blocks/0/10  
http://localhost:8080/blocks/500 
http://localhost:8080/blocks/SOME_BIG_BLOCK_NUMBER (will result in NOT FOUND json response)  
http://localhost:8080/blocks/500010/transactions (shows some block transactions)
http://localhost:8080/blocks/100010/transactions (shows there are no transactions for given block number)

 
