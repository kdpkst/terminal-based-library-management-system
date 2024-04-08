package Models.User;

import java.util.ArrayList;
import java.util.List;

import Models.Book.book;

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

    public boolean register(String username, String password){
        return true;
    }

    public boolean borrowBook(int cid){
        return true;
    }

    public boolean returnBook(int cid){
        return true;
    }

}
