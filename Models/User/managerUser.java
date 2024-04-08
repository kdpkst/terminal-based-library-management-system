package Models.User;

import java.util.ArrayList;
import java.util.List;

import Models.Book.book;

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

    @Override
    public boolean login(String username, String password, int type) {
        return true;
    }

    @Override
    public List<book> viewAllBooks(){
        return new ArrayList<>();
    }

    @Override
    public List<book> viewAllBookCopies(int bid) {
        return new ArrayList<>();
    }

    @Override
    public List<book> searchBooks() {
        return new ArrayList<>();
    }

    public boolean addBook(String title, String author, String genre){
        return true;
    }

    public boolean removeBook(int cid){
        return true;
    }

    public List<user> viewAllUsers(){
        return new ArrayList<>();
    }

    // use Strategy pattern to switch between different search mechanism
    // may consider support searching by uid, username 
    public List<user> searchUser(){
        return new ArrayList<>();
    }

}
