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
pattern** to notify users when a book they want becomes available; …)

## Database Design
table 1: **users**     
| uid | username | password | type | bid_want |
|:---:|:--------:|:--------:|:----:|:--------:|
> **type**: manager or normal user  
> **bid_want**: record a list of unavailable books the user wants. Otherwise leave it empty  
> primary key: uid; username should be unique

table 2: **books**
| bid | title | author | genre | quantity_available |
|:---:|:-----:|:------:|:-----:|:------------------:|
> primary key: bid  
> (title, author) should be unique

table 3: **book_copies** 
| cid | bid | status |
|:---:|:---:|:------:|
> **status**: available or not  
> primary key: cid

table 4: **transactions**
| tid | uid | cid | transaction_date | status |
|:---:|:---:|:---:|:----------------:|:------:|
> **status**: completed or incompleted  
> primary key: tid

table 5: **fines** 
| fid | uid | amount | status |
|:---:|:---:|:------:|:---------:|
> insert a record of fine when a user return an overdue book  
> primary key: fid
