package Models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
