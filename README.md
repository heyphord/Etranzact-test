
## By Hayford Owusu Ansah

# ETRANZACT SOFTWARE INTERVIEW REQUIREMENTS DOCUMENT

## PROJECT OBJECTIVE / GOAL

Build a simple banking application that allows customers to manage their accounts, perform transactions, and view their account details.

## FUNCTIONAL REQUIREMENTS

1. Customers should be able to sign up. I assumed customers will use a Ghana Card ID to sign up. I also assumed a primary account will be created for the user on first sign up. Additional account types can be created later.

2. Customers should be able to log in. I assumed users will use their Ghana Card number and PIN for authentication. A simple token will be returned when authentication succeeds.

3. Customers should be able to deposit into their accounts. The token must be valid before allowing a deposit. Although a future version could allow others to deposit into that account.

4. Customers should be able to withdraw money from their accounts. Users should only be allowed to perform this operation if the token is valid and not expired.

5. Customers should be able to see their transaction history.

6. Customers should be able to change their PIN. This feature was added to meet the non-functional security requirements of this project.

## NON-FUNCTIONAL REQUIREMENTS

1. **Simplicity:** The system should be easy to use. API interfaces should be predictable and intuitive so the frontend team can integrate easily.

2. **Security:** We are dealing with sensitive information, such as money. Only authorized individuals should be allowed to access a user's balance or withdraw funds. Future versions of this app could allow anyone to deposit into an account.

Another security measure is capturing the Ghana Card at sign-up. This helps us identify the user in case of malicious activity.

An additional feature lets customers change their PIN after log-in. This supports the **secure** non-functional requirement.

## DATABASE DESIGN

### Entities

#### Customers

Stores details of signed-up customers.

#### Accounts

List of all customer accounts. Key fields include `is_primary`, account type, and balance.

#### Transactions

Records all credit and debit activity on accounts.

### Entity attributes

#### Customer

- `id` — PK  
- `fname`  
- `lname`  
- `dob`  
- `ghanacard_number`  
- `pin`  

#### Accounts

- `id` — PK  
- `type`  
- `is_primary`  
- `balance`  
- `customer_id` — FK  

#### Transactions

- `id` — PK  
- `type` (DEPOSIT / WITHDRAW)  
- `amount`  
- `balanceBefore`  
- `balanceAfter`  
- `occurredAt`  
- `account_id` — FK  

## Proposed APIs

1. `{{baseUrl}}/api/v1/customers/signup`
2. `{{baseUrl}}/api/v1/customers/login`
3. `{{baseUrl}}/api/v1/customers/pin` (change PIN, after login)
4. `{{baseUrl}}/api/v1/accounts/:accountId/balance`
5. `{{baseUrl}}/api/v1/accounts/:accountId/deposit`
6. `{{baseUrl}}/api/v1/accounts/:accountId/withdraw`
7. `{{baseUrl}}/api/v1/accounts/:accountId/transactions?page=0&size=20`

## Steps to run

1. Clone this project from my repo: https://github.com/heyphord/Etranzact-test.git


2. Create your own `application.properties` file at `src/main/resources/application.properties`. Ideally this should **not** be committed to the repository, but I added one for simplicity. The project uses an in-memory database, so you do not need to run a separate database server to test it.


3. Build and run this project. Your IDE will install the project’s dependencies.


4. An OpenAPI file is included for testing this application. Import it into Postman (or similar). It is at the project root: `openapi.yaml`.


### Looking forward to hearing from you again, Hayford
