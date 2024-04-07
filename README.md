# Library Management System
A console-based library management system with a particular focus on implementation of different design patterns.

## Minimum Requirements
- add books 
- remove books 
- user registration 
- check books in 
- check books out 
- use **Singleton pattern** to handle a database connection object (file or variable) 
- choose a use case for **Factory method** or **Builder pattern** (e.g., use Factory method 
to create different types of books or users; …) 
- use **Structural patterns** to assign fines to overdue books 
- use **Behavioral patterns** to handle some of application logic (e.g., use **Strategy 
pattern** to switch between different search mechanism for books; use **Observer 
pattern** to notify uses when a book they want becomes available; …)

## Database Design
table 1: **users**     
| uid | username | password | type |
|:---:|:--------:|:--------:|:------:|

table 2: **books**
| bid | title | author | genre | quantity_available |
|:---:|:-----:|:------:|:-----:|:------------------:|

table 3: **transactions**
| tid | uid | bid | transaction_type | transaction_date |
|:---:|:---:|:---:|:----------------:|:----------------:|

table 4: **fines** 
| fid | tid | amount | fine_date |
|:---:|:---:|:------:|:---------:|
