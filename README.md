
## BY HAYFORD OWUSU ANSAH
# ETRANZACT SOFTWARE INTERVIEW REQUIRMENTS DOCUMENTS 

## PROJECT OBJECTIVE/GOAL
Build a simple banking application that allows customers to manage their ccounts, perform transactions and view their account details.

## FUNCTIONAL REQUIREMENTS

1.  Customers should be able to signup. I assumed a primary account will be created for the user on first signup. subsequent account type be created later. 

2.  Customer should be able to login (i assumed users will use account number and PIN for authentication. a simple token will be returned if authentication is true)

3.  Customer should be able to make deposit into their accounts (Token should be valid before allowing withdrawal. Althogh future version, will be open to allow others to deposite into that account. )


4.  Customers should be able to withdral money from their accounts  (Users must only be allowed to do this operation only if token is valid and not expired) 

5.  Customer should be able to see their transaction history


## NON FUNCTIONAL REQUIREMENTS

1.  Simplicity: The system should be easy to use. API interfaces should be predictable and intuitive to so frontend team can easily integrate

2. Secure: Since we are dealing with a sensitive information like money. Only authorized individualts should be allowed to access the users balance, or withdraw. Future versions of this app can support anyone depositing into the account.

Another secure feature added is the addition of ghana card during signup. this will help us track the user incase of malicious activities.

## DATABASE DESIGN

### Entities
#### Customers : Stores details of signup up customers

#### Accounts : List of all customer accounts. key fields include is_primary , account_type and balanace

#### Transactions : Records all Credit and Debit activities on the Accounts

### Entity-Attributes

#### Customer
id -PK
fname
laname
dob
ghanacard_number
pin

#### Accounts
id- PK
type
is_primary
balance
customer_id- FK

#### Transactions
id- PK
type (DEPOSIT/WITHDRAWAL)
is_primary
amount
balanceBefore
balanceAfter
occuredAt
account_id- FK


## Proposed APIs

1.  {{baseUrl}}/api/v1/customers/signup
2.  {{baseUrl}}/api/v1/customers/login
3.  {{baseUrl}}/api/v1/accounts/:accountId/balance
4.  {{baseUrl}}/api/v1/accounts/:accountId/deposit
5.  {{baseUrl}}/api/v1/accounts/:accountId/withdraw
6.  {{baseUrl}}/api/v1/accounts/:accountId/transactions?page=0&size=20


# Steps to run
1. Clone this project from my repo: https://github.com/heyphord/Etranzact-test.git

2. create your own application.properties file at src/main/resources/application.properties. Ideally this should NOT be commited to the repo. but i added it for simplicy. I use in memeory database so you wont need to spin up a database server to test this project

3. Build and run this project. Your IDE will automatically install all the projects dependencies

4. I have also exported the openapi file used to test this application. Just import it into postman and you are good to go. File is located at the root of this project at /openapi.yaml

