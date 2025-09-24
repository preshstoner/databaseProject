Class Exercise: Mini Library Management System (Java + MySQL)

Instructions:

You are required to build a console-based Library Management System in Java that connects to a MySQL database. The application should allow the user to add, view, update, and delete book records from the database.

Database Setup

Create a database called library_db.

Inside it, create a table called books with the following structure:

id (INT, Primary Key, Auto Increment)

title (VARCHAR(100))

author (VARCHAR(50))

year (INT)

available (BOOLEAN)

Program Requirements

Write a Java program that connects to the library_db using JDBC and performs the following operations:

Add a new book

The user should be able to enter a book’s title, author, year, and availability (true/false).

Save this data into the books table.

View all books

Retrieve and display all records from the books table in a clear format.

Update book availability

Allow the user to enter the id of a book and update whether it is available or not.

Delete a book

Allow the user to enter the id of a book and delete it from the table.

Exit the program

End the application and close the database connection.
