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

    public boolean addBook(String title, String author, String genre){
        dbSingleton dbConnctor = dbSingleton.getInstance();
        List<Map<String,String>> bookRecords= dbConnctor.preciseSearch("books", "title", title);
        
        List<Map<String, String>> filteredBookRecords = new ArrayList<>();

        for (Map<String, String> bookRecord : bookRecords) {
            String authorFromRecord = bookRecord.get("author");
            if (author.equals(authorFromRecord)) {
                filteredBookRecords.add(bookRecord);
            }
        }


        if(filteredBookRecords.size()==0){
            int bid=dbConnctor.getNextId("books");
            Map<String, String> bookInserted = new HashMap<String, String>();
            bookInserted.put("bid", String.valueOf(bid));
            bookInserted.put("title", title);
            bookInserted.put("author", author);
            bookInserted.put("genre", genre);
            List<Map<String, String>> bookresult = new ArrayList<>();
            bookresult.add(bookInserted);
            Boolean bookInsertflag =dbConnctor.insert("books",bookresult);
            if(!bookInsertflag){
                return false;
            }
            filteredBookRecords.add(bookInserted);
        }

        String bidInsert=filteredBookRecords.get(0).get("bid");
        int cid=dbConnctor.getNextId("book_copies");
        Map<String, String> copyInserted = new HashMap<String, String>();
            copyInserted.put("cid", String.valueOf(cid));
            copyInserted.put("bid", bidInsert);
            copyInserted.put("status", "1");
            List<Map<String, String>> copyresult = new ArrayList<>();
            copyresult.add(copyInserted);
            Boolean copyInsertflag =dbConnctor.insert("books_copies",copyresult);
            if(!copyInsertflag){
                return false;
            }



        return true;
    }

    public boolean removeBook(int cid){
        dbSingleton dbConnctor = dbSingleton.getInstance();
        List<Map<String, String>> bookCopies = dbConnctor.preciseSearch("book_copies", "cid", String.valueOf(cid));
        if(bookCopies.size()==0){return false;}
        String status=bookCopies.get(0).get("status");
        if(status.equals("0")){return false;}
        String bid=bookCopies.get(0).get("bid");


        int deleteCopynumber=dbConnctor.delete("book_copies","cid",String.valueOf(cid));
        if(deleteCopynumber<=0){
            return false;
        }

        List<Map<String,String>> copyRecords= dbConnctor.preciseSearch("book_copies", "bid", bid);
        if(copyRecords.size()==0){
            int deleteBooknumber=dbConnctor.delete("books", "bid", bid);
            if(deleteBooknumber<=0){
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
            String userType = userRecord.get("userType");
            // Assuming userType is stored as an integer where 0 corresponds to normalUser and 1 corresponds to managerUser
            if (Integer.parseInt(userType) == type) {
                switch (type) {
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
