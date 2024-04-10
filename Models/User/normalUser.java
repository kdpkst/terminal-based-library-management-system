package Models.User;

import java.util.ArrayList;
import java.util.List;

import DatabaseConnection.dbSingleton;
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

    public normalUser(){
        this.type = 0;
    }

    // an example, waiting for further modification
    @Override
    public boolean login(String username, String password) {
        // dbSingleton dbConnctor = dbSingleton.getInstance();
        // List<String> userRecord = dbConnctor.preciseSearch("users", "username", username);
        // if (userRecord.size() == 1){
        //     int id = Integer.parseInt(userRecord.get(0).split(",")[0]);
        //     String passwd = userRecord.get(0).split(",")[2];
        //     int uType = Integer.parseInt(userRecord.get(0).split(",")[3]);
        //     String booksWant = userRecord.get(0).split(",")[4];
        //     if (passwd.equals(password) && uType == 0){
        //         this.setUid(id);
        //         this.setUsername(username);
        //         this.setPassword(password);
        //         this.setBidWant(booksWant);
        //         return true;
        //     }
        //     else{
        //         // input password incorrect or it is manager user
        //         return false;
        //     }
        // }
        // else{
        //     // username does not exist
        //     return false;
        // }
        return true;
    }

    @Override
    public List<book> viewAllBooks(){
        return new ArrayList<>();
    }

    @Override
    public List<book> viewAllCopiesforOneBook(int bid) {
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
