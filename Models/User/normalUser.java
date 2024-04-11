package Models.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

public class normalUser implements user{
    
    private int uid;
    private String username;
    private String password;
    private String bidWant;
    private final int type;
    
    public normalUser(int uid, String username, String password, String bidWant){
        this.uid = uid;
        this.username = username;
        this.password = password;
        this.bidWant = bidWant;
        this.type = 0;
    }

    public normalUser(){
        this.type = 0;
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
            if (passwd.equals(password) && uType == 0){
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

    /**
     * @param username Username entered when the user registers 
     * @param password Password entered when the user registers
     * @return An integer indicating the registration status:
     *         1 if successfully inserting
     *         0 if error occurred while inserting 
     *         -1 if username is duplicate
     */
    public int register(String username, String password){
        dbSingleton dbConnctor = dbSingleton.getInstance();
        List<Map<String, String>> userRecords = dbConnctor.preciseSearch("users", "username", username);
        // the username must be unique
        if (userRecords.size() == 0){
            int uid = dbSingleton.getNextId("users");
            Map<String, String> recordInserted = new HashMap<String, String>();
            recordInserted.put("uid", String.valueOf(uid));
            recordInserted.put("username", username);
            recordInserted.put("password", password);
            recordInserted.put("type", String.valueOf(this.getType()));
            recordInserted.put("bid_want", "none");
    
            List<Map<String, String>> result = new ArrayList<>();
            result.add(recordInserted);
    
            this.setUid(uid);
            this.setUsername(username);
            this.setPassword(password);
            this.setBidWant("none");
    
            boolean inserted = dbConnctor.insert("users", result);
            if (inserted){
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

    public boolean borrowBook(int cid) {
        dbSingleton dbConnector = dbSingleton.getInstance();
        Map<String, String> bookCopyRecord = getBookCopyRecord(dbConnector, cid);
        if (bookCopyRecord == null) {
            // cid does not exist
            return false;
        }
        if (!isBookAvailable(bookCopyRecord)) {
            // book copy unavailable
            return false;
        }

        updateBookCopyStatus(dbConnector, cid, "0");

        int bid = Integer.parseInt(bookCopyRecord.get("bid"));
        updateQuantityAvailable(dbConnector, bid, -1);

        insertTransactionRecord(dbConnector, cid, "borrow", "incomplete");

        return true;
    }

    public boolean returnBook(int cid){
        dbSingleton dbConnector = dbSingleton.getInstance();
        
        // Find incomplete transaction for the given cid and current user
        Map<String, String> incompleteTransaction = findIncompleteTransaction(dbConnector, cid, String.valueOf(this.getUid()));
        if (incompleteTransaction == null) {
            // No incomplete transaction found for the given cid and current user
            return false;
        }
        
        // Update incomplete transaction status to "completed" and get transaction time
        LocalDate transactionDate = completeTransaction(dbConnector, incompleteTransaction);
        
        // Update book copy and book records
        updateBookCopyAndBook(dbConnector, cid);
        
        // Calculate fine if the book is overdue
        calculateFine(transactionDate);
        
        return true;
    }

    private Map<String, String> getBookCopyRecord(dbSingleton dbConnector, int cid) {
        List<Map<String, String>> bookcopyList = dbConnector.preciseSearch("book_copies", "cid", String.valueOf(cid));
        return bookcopyList.size() == 1 ? bookcopyList.get(0) : null;
    }

    private boolean isBookAvailable(Map<String, String> bookCopyRecord) {
        int status = Integer.parseInt(bookCopyRecord.get("status"));
        return status == 1;
    }

    private void updateBookCopyStatus(dbSingleton dbConnector, int cid, String status) {
        Map<String, String> bookCopyRecord = new HashMap<>();
        bookCopyRecord.put("status", status);
        List<Map<String, String>> updatedbookcopyList = new ArrayList<>();
        updatedbookcopyList.add(bookCopyRecord);
        dbConnector.update("book_copies", "cid", String.valueOf(cid), updatedbookcopyList);
    }

    private void updateQuantityAvailable(dbSingleton dbConnector, int bid, int value) {
        List<Map<String, String>> bookList = dbConnector.preciseSearch("books", "bid", String.valueOf(bid));
        if (!bookList.isEmpty()) {
            Map<String, String> bookRecord = bookList.get(0);
            int quantity_available = Integer.parseInt(bookRecord.get("quantity_available")) + value;
            bookRecord.put("quantity_available", String.valueOf(quantity_available));
            List<Map<String, String>> updatedbookList = new ArrayList<>();
            updatedbookList.add(bookRecord);
            dbConnector.update("books", "bid", String.valueOf(bid), updatedbookList);
        }
    }

    private void insertTransactionRecord(dbSingleton dbConnector, int cid, String transactionType, String status) {
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, String> recordInserted = new HashMap<>();
        recordInserted.put("tid", String.valueOf(dbSingleton.getNextId("transactions")));
        recordInserted.put("uid", String.valueOf(this.getUid()));
        recordInserted.put("cid", String.valueOf(cid));
        recordInserted.put("transaction_date", getCurrentDate());
        recordInserted.put("status", status);
        data.add(recordInserted);
        dbConnector.insert("transactions", data);
    }

    private String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private Map<String, String> findIncompleteTransaction(dbSingleton dbConnector, int cid, String uid) {
        List<Map<String, String>> transactions = dbConnector.preciseSearch("transactions", "cid", String.valueOf(cid));
        for (Map<String, String> transaction : transactions) {
            if (transaction.get("status").equals("incomplete") && transaction.get("uid").equals(uid)) {
                return transaction;
            }
        }
        return null;
    }
    
    private LocalDate completeTransaction(dbSingleton dbConnector, Map<String, String> incompleteTransaction) {
        String tid = incompleteTransaction.get("tid");
        incompleteTransaction.put("status", "completed");
        dbConnector.update("transactions", "tid", tid, List.of(incompleteTransaction));
        // Get transaction date
        String transactionDateStr = incompleteTransaction.get("transaction_date");
        return LocalDate.parse(transactionDateStr, DateTimeFormatter.ISO_LOCAL_DATE);
    }
    
    private void updateBookCopyAndBook(dbSingleton dbConnector, int cid) {
        // Update book copy status to available
        Map<String, String> bookCopyRecord = getBookCopyRecord(dbConnector, cid);
        bookCopyRecord.put("status", "1");
        dbConnector.update("book_copies", "cid", String.valueOf(cid), List.of(bookCopyRecord));
    
        // Increment quantity available for the book
        int bid = Integer.parseInt(bookCopyRecord.get("bid"));
        Map<String, String> bookRecord = getBookRecord(dbConnector, bid);
        int quantityAvailable = Integer.parseInt(bookRecord.get("quantity_available")) + 1;
        bookRecord.put("quantity_available", String.valueOf(quantityAvailable));
        dbConnector.update("books", "bid", String.valueOf(bid), List.of(bookRecord));
    }

    private Map<String, String> getBookRecord(dbSingleton dbConnector, int bid) {
        List<Map<String, String>> bookList = dbConnector.preciseSearch("books", "bid", String.valueOf(bid));
        if (!bookList.isEmpty()) {
            return bookList.get(0);
        } else {
            return null; 
        }
    }
    
    private void calculateFine(LocalDate transactionDate) {
        LocalDate now = LocalDate.now();
        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(transactionDate, now);
        if (daysBetween > 30) {
            // Calculate and assign $5 fine
            // Example: Assuming there's a method to assign fine to the user
            // assignFineToUser(5);
        }
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
