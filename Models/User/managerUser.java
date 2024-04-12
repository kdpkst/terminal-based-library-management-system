package Models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DatabaseConnection.dbSingleton;

import Models.Book.book;
import Models.BookCopy.bookCopy;
import Strategy.SearchByAuthor;
import Strategy.SearchByGenre;
import Strategy.SearchByTitle;
import Factory.BookFactory.*;
import Factory.UserFactory.managerUserFactory;
import Factory.UserFactory.normalUserFactory;

public class managerUser implements user{

    private int uid;
    private String username;
    private String password;
    private String bidWant;
    private final int type;

    public managerUser(int uid, String username, String password, String bidWant){
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.bidWant = bidWant;
        this.type = 1;
    }

    public managerUser(){
        this.type = 1;
    }

    @Override
    public int login(String username, String password) {
        dbSingleton dbConnctor = dbSingleton.getInstance();
        List<Map<String, String>> userRecords = dbConnctor.preciseSearch("users", "username", username);
        if (userRecords.size() == 1){
            int id = Integer.parseInt(userRecords.get(0).get("uid")); 
            String passwd = userRecords.get(0).get("password");
            int uType = Integer.parseInt(userRecords.get(0).get("type"));
            String booksWant = userRecords.get(0).get("bid_want");
            if (passwd.equals(password) && uType == 1){
                this.setUid(id);
                this.setUsername(username);
                this.setPassword(password);
                this.setBidWant(booksWant);
                return 1;
            }
            else{
                return 0;
            }
        }
        else{
            return -1;
        }
    }

    @Override
    public List<book> viewAllBooks(){
        dbSingleton dbConnctor = dbSingleton.getInstance();
        List<Map<String, String>> books = dbConnctor.listAll("books");
        List<book> booksList = new ArrayList<>();
        for (int i = 0; i < books.size(); i++){
            Map<String, String> bookRecord = books.get(i);
            int bid = Integer.parseInt(bookRecord.get("bid"));
            String title = bookRecord.get("title");
            String author = bookRecord.get("author");
            String genre = bookRecord.get("genre");
            int quantity_available = Integer.parseInt(bookRecord.get("quantity_available"));
            switch(genre){
                case "science":
                    bookFactory sBookFactory = new scienceBookFactory();
                    book scienceBook = sBookFactory.createBook(bid, title, author, genre, quantity_available);
                    booksList.add(scienceBook);
                    break;
                case "business":
                    bookFactory bBookFactory = new businessBookFactory();
                    book businessBook = bBookFactory.createBook(bid, title, author, genre, quantity_available);
                    booksList.add(businessBook);
                    break;
                case "novel":
                    bookFactory nBookFactory = new novelBookFactory();
                    book novelBook = nBookFactory.createBook(bid, title, author, genre, quantity_available);
                    booksList.add(novelBook);
                    break;
                case "history":
                    bookFactory hBookFactory = new historyBookFactory();
                    book historyBook = hBookFactory.createBook(bid, title, author, genre, quantity_available);
                    booksList.add(historyBook);
                    break;
            }
        }

        return booksList;
    }

    @Override
    public List<bookCopy> viewAllCopiesforOneBook(int bid) {
        dbSingleton dbConnctor = dbSingleton.getInstance();
        List<Map<String, String>> bookCopies = dbConnctor.preciseSearch("book_copies", "bid", String.valueOf(bid));
        List<bookCopy> bookCopiesList = new ArrayList<>();
        for (int i = 0; i < bookCopies.size(); i++){
            Map<String, String> bookCopiesRecord = bookCopies.get(i);
            int cid = Integer.parseInt(bookCopiesRecord.get("cid"));
            int status = Integer.parseInt(bookCopiesRecord.get("status"));
            bookCopy bookCopyObject = new bookCopy(cid, bid, status);
            bookCopiesList.add(bookCopyObject);
        }
        return bookCopiesList;
    }


    @Override
    public List<book> searchBooks(String searchKey, String searchValue) {
        List<book> bookList = new ArrayList<>();
        switch (searchKey) {
            case "author":
                SearchByAuthor searchByAuthor = new SearchByAuthor();
                bookList = searchByAuthor.search(searchValue);
            case "genre":
                SearchByGenre searchByGenre = new SearchByGenre();
                bookList = searchByGenre.search(searchValue);
                break;
            case "title":
                SearchByTitle searchByTitle = new SearchByTitle();
                bookList = searchByTitle.search(searchValue);
                break;
            default:
                return null;
        }
        return bookList;
    }

    
    public boolean addBook(String title, String author, String genre) {
        dbSingleton dbConnector = dbSingleton.getInstance();
    
        // Step 1: Search for the book by title and author
        List<Map<String, String>> existingBooks = dbConnector.preciseSearch("books", "title", title);
        String bid = null;
        boolean bookExists = false;
    
        for (Map<String, String> book : existingBooks) {
            if (book.get("author").equals(author)) {
                bid = book.get("bid");
                bookExists = true;
                break;
            }
        }
    
        // Step 2: If the book does not exist, add it to the books table
        if (!bookExists) {
            int newBid = dbConnector.getNextId("books");
            Map<String, String> newBook = new HashMap<>();
            newBook.put("bid", String.valueOf(newBid));
            newBook.put("title", title);
            newBook.put("author", author);
            newBook.put("genre", genre);
            newBook.put("quantity_available", "1");  // Start with a quantity of 1
            List<Map<String, String>> booksToAdd = new ArrayList<>();
            booksToAdd.add(newBook);
            boolean bookAdded = dbConnector.insert("books", booksToAdd);
            if (!bookAdded) {
                return false; // Failed to add the new book
            }
            bid = String.valueOf(newBid); // Update bid for new book
        } else {
            // If the book exists, update the quantity available
            List<Map<String, String>> booksToUpdate = dbConnector.preciseSearch("books", "bid", bid);
            if (!booksToUpdate.isEmpty()) {
                Map<String, String> bookToUpdate = booksToUpdate.get(0);
                int currentQuantity = Integer.parseInt(bookToUpdate.get("quantity_available"));
                bookToUpdate.put("quantity_available", String.valueOf(currentQuantity + 1));
                List<Map<String, String>> updateList = new ArrayList<>();
                updateList.add(bookToUpdate);
                dbConnector.update("books", "bid", bid, updateList);
            }
        }
    
        // Step 3: Add a new book copy
        int newCid = dbConnector.getNextId("book_copies");
        Map<String, String> newCopy = new HashMap<>();
        newCopy.put("cid", String.valueOf(newCid));
        newCopy.put("bid", bid);
        newCopy.put("status", "1");  // Assuming status '1' means 'available'
        List<Map<String, String>> copiesToAdd = new ArrayList<>();
        copiesToAdd.add(newCopy);
        boolean copyAdded = dbConnector.insert("book_copies", copiesToAdd);
        return copyAdded;
    }
    

    public boolean removeBook(int cid) {
        dbSingleton dbConnector = dbSingleton.getInstance();
        
        // Step 1: Find the book copy to get the 'bid' and check 'status'
        List<Map<String, String>> bookCopies = dbConnector.preciseSearch("book_copies", "cid", String.valueOf(cid));
        if (bookCopies.isEmpty()) {
            System.out.println("No book copy found with cid: " + cid);
            return false;
        }
    
        String bid = bookCopies.get(0).get("bid");
        String status = bookCopies.get(0).get("status");
        
        // Only proceed if status is not '0'
        if ("0".equals(status)) {
            System.out.println("Cannot delete book copy with cid: " + cid + " because it is inactive.");
            return false;
        }
    
        // Step 2: Delete the book copy
        int copiesDeleted = dbConnector.delete("book_copies", "cid", String.valueOf(cid));
        if (copiesDeleted == 0) {
            System.out.println("Failed to delete the book copy.");
            return false;
        }
    
        // Step 3: Retrieve the book record to update quantity
        List<Map<String, String>> bookRecords = dbConnector.preciseSearch("books", "bid", bid);
        if (bookRecords.isEmpty()) {
            System.out.println("No book record found with bid: " + bid);
            return false;
        }
        Map<String, String> bookRecord = bookRecords.get(0);
        int currentQuantity = Integer.parseInt(bookRecord.get("quantity_available"));
        if (currentQuantity > 0) {
            bookRecord.put("quantity_available", String.valueOf(currentQuantity - 1));
            List<Map<String, String>> updatedRecords = new ArrayList<>();
            updatedRecords.add(bookRecord);
            dbConnector.update("books", "bid", bid, updatedRecords);
        }
    
        // Step 4: Check if there are more active copies with the same 'bid'
        List<Map<String, String>> remainingCopies = dbConnector.preciseSearch("book_copies", "bid", bid);
        boolean anyActiveCopies = remainingCopies.stream()
            .anyMatch(copy -> !"0".equals(copy.get("status"))); // Check for any active copies
    
        if (!anyActiveCopies && currentQuantity == 1) { // If no more active copies and it was the last one
            int booksDeleted = dbConnector.delete("books", "bid", bid);
            if (booksDeleted == 0) {
                System.out.println("Failed to delete the book record.");
                return false;
            }
        }
    
        return true;
    }
    
    
    

    public List<user> viewAllUsers(){
        dbSingleton dbConnctor = dbSingleton.getInstance();
        List<Map<String, String>> users = dbConnctor.listAll("users");
        List<user> usersList = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            Map<String, String> userRecord = users.get(i);
            int uid = Integer.parseInt(userRecord.get("uid"));
            String username = userRecord.get("username");
            String password = userRecord.get("password");
            String bidWant=userRecord.get("bid_want");
            String userType = userRecord.get("type");
            int Type= Integer.parseInt(userType);
            // Assuming userType is stored as an integer where 0 corresponds to normalUser and 1 corresponds to managerUser
            
            switch (Type) {
                case 0:
                    normalUserFactory normalUserFactory = new normalUserFactory();
                    user normalUser = normalUserFactory.createUser(uid, username, password,bidWant);
                    usersList.add(normalUser);
                    break;
                case 1:
                    managerUserFactory managerUserFactory = new managerUserFactory();
                    user managerUser = managerUserFactory.createUser(uid, username, password,bidWant);
                    usersList.add(managerUser);
                    break;
            }
            
        }
        return usersList;
    }

    // use Strategy pattern to switch between different search mechanism
    // may consider support searching by uid, username 
    public List<user> searchUser(){
        return new ArrayList<>();
    }

    
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBidWant() {
        return bidWant;
    }

    public void setBidWant(String bidWant) {
        this.bidWant = bidWant;
    }

    public int getType() {
        return type;
    }

}
