/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package databaseProject;

import java.sql.Connection; 
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author USER
 */
public class jdbcClassWork1 {

    /**
     * The main method to run the application.
     * It's connect to the database and provides a menu-driven interface
     * @param args the command line arguments (not used).
     */
    public static void main(String[] args) {
        // Use try-with-resources to ensure connection and scanner are closed auomatically
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library_db", "root", "23051999");
                Scanner scanner = new Scanner(System.in)){
                System.out.println("Connected to the libary database sucessfully");
                
                boolean exit = false;
                while (!exit){
                    //Display the menu
                    System.out.println("\n---Library Management System ---");
                    System.out.println("1. Add a new book");
                    System.out.println("2. View all books");
                    System.out.println("3. Update book availability");
                    System.out.println("4. Delete a book");
                    System.out.println("5. Exit");
                    System.out.print("Enter your choice: ");
                    
                    try{
                        int choice = scanner.nextInt();
                        scanner.nextLine(); //Consume newline left-over
                        
                        switch (choice){
                            case 1: 
                                addBook(con, scanner);
                                break;
                            case 2:
                                viewAllBooks(con);
                                break;
                            case 3: 
                                updateBookAvailability(con, scanner); 
                                break;
                            case 4: 
                                deleteBook(con, scanner);
                                break;
                            case 5: 
                                exit = true;
                                System.out.println("Exiting the program. Goodbye!"); 
                                break; 
                            default:
                                System.out.println("Invalid choice. Please enter a number between 1 and 5."); 
                        }
                    }catch (InputMismatchException e){
                        System.out.println("Invalid input. Please enter a valid number.");
                        scanner.nextLine(); //Consume the invalid input to avoid
                    }
                }
        }catch (SQLException e){
            System.err.println("An SQL error occurred.");
            e.printStackTrace();
        }
    }
    
    /**
     * Adds a new book to the database.
     * 
     * @param con The database connection.
     * @param scanner The scanner for user input. 
     */
    private static void addBook (Connection con, Scanner scanner){
        try (PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO books(id, title, author, year, available) VALUES (?, ?, ?, ?, ?)")){
            System.out.println("\n--- Add a New Book ---");
            System.out.print("Enter Book ID(integer): ");
            int id = scanner.nextInt();
            scanner.nextLine(); //Consume newline
            System.out.print("Enter Book Title: ");
            String title = scanner.nextLine();
            System.out.print("Enter Author name: ");
            String author = scanner.nextLine();
            System.out.print("Enter Publication Year(integer): ");
            int year = scanner.nextInt();
            System.out.print("Is the book available? (true/false): ");
            boolean available = scanner.nextBoolean();
            scanner.nextLine(); // consume newline
            
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, title);
            preparedStatement.setString(3, author);
            preparedStatement.setInt(4, year);
            preparedStatement.setBoolean(5, available);
            
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0){
                System.out.println("Book added successfully! ");
            }else{
                System.out.println("Failed to add the book. ");
            }
        }catch (SQLException e){
            System.err.println("Error adding books: " + e.getMessage());
        }
    }
    
    /**
     * Retrieves and display all books from the database.
     * 
     * @param con The database connection. 
     */
    private static void viewAllBooks(Connection con){
        try (PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM books ORDER BY id");
                ResultSet rs = preparedStatement.executeQuery()){
            
            System.out.println("\n--- All Books in the Libary ---");
            System.out.printf("%-5s | %-30s | %-20s | %-5s |%s%n", "ID", "Title", "Author", "Year", "Available");
            System.out.println("-----------------------------------------------------------------------------------------------");
            
            if (!rs.isBeforeFirst()){
                System.out.println("No books found in the database. ");
            }
            
            while(rs.next()){
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int year = rs.getInt("year");
                boolean available = rs.getBoolean("available");
                System.out.printf("%-5d | %-30s | %-20s | %-5d | %s%n", id, title, author, year, available);
            }
        }catch (SQLException e){
            System.err.println("Error viewing books: " + e.getMessage());
        }
    }
    
    /**
     * Updates the availability status of a book.
     * 
     * @param con The database connection.
     * @param scanner The scanner for user input,
     */
    private static void updateBookAvailability(Connection con, Scanner scanner){
        try (PreparedStatement preparedStatement = con.prepareStatement("UPDATE books SET available = ? WHERE id = ?")){
            System.out.println("/n--- Update Book Availability ---");
            System.out.print("Enter the ID of the book to update: ");
            int id = scanner.nextInt();
            System.out.print("Is the book now available? (true/false): ");
            boolean available = scanner.nextBoolean(); 
            scanner.nextLine(); //Consume newline
            
            preparedStatement.setBoolean(1, available);
            preparedStatement.setInt(2, id);
            
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0){
                System.out.println("Book availability updated successfully!");
            }else {
                System.out.println("No book found with ID: " + id);
            }
        }catch (SQLException e){
            System.err.println("Error updating book: "+ e.getMessage());
        }
    }
    
    /**
     * Deletes a book from the database.
     * 
     * @param con The database connection.
     * @param scanner The scanner for user input.
     */
    private static void deleteBook(Connection con, Scanner scanner){
        try (PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM books WHERE id = ?")){
            System.out.println("\n--- Delete a Book ---");
            System.out.print("Enter the ID of the book to delete: ");
            int id = scanner.nextInt();
            scanner.nextLine(); //Consume newLine
            
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0){
                System.out.println("Book with ID " +id + " deleted successfully!");
            }else{
                System.out.println("No book found with ID: " + id);
            }
        }catch (SQLException e){
            System.err.println("Error deleting book: " + e.getMessage());
        }
    }
}
