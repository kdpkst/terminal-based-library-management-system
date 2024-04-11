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
import Factory.BookFactory.*;
import Models.Transaction.transaction;

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
    public List<book> searchBooks() {
        return new ArrayList<>();
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
        decrementQuantityAvailableByOne(dbConnector, bid);

        insertTransactionRecord(dbConnector, cid);

        return true;
    }

    public boolean returnBook(int cid){
        dbSingleton dbConnector = dbSingleton.getInstance();


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

    private void decrementQuantityAvailableByOne(dbSingleton dbConnector, int bid) {
        List<Map<String, String>> bookList = dbConnector.preciseSearch("books", "bid", String.valueOf(bid));
        if (!bookList.isEmpty()) {
            Map<String, String> bookRecord = bookList.get(0);
            int quantity_available = Integer.parseInt(bookRecord.get("quantity_available")) - 1;
            bookRecord.put("quantity_available", String.valueOf(quantity_available));
            List<Map<String, String>> updatedbookList = new ArrayList<>();
            updatedbookList.add(bookRecord);
            dbConnector.update("books", "bid", String.valueOf(bid), updatedbookList);
        }
    }

    private void insertTransactionRecord(dbSingleton dbConnector, int cid) {
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, String> recordInserted = new HashMap<>();
        recordInserted.put("tid", String.valueOf(dbSingleton.getNextId("transactions")));
        recordInserted.put("uid", String.valueOf(this.getUid()));
        recordInserted.put("cid", String.valueOf(cid));
        recordInserted.put("transaction_type", "borrow");
        recordInserted.put("transaction_date", getCurrentDate());
        recordInserted.put("status", "incomplete");
        data.add(recordInserted);
        dbConnector.insert("transactions", data);
    }

    private String getCurrentDate() {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
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
