package Models;

public class dbSingleton {
    private static dbSingleton instance;

    private dbSingleton(){

    }

    public static dbSingleton getInstance(){
        if (instance == null) {
            instance = new dbSingleton();
        }
        return instance;
    }

    // public void insert(){

    // }

    // public void delete(){

    // }

    // public void update(){

    // }

    // public void search(){

    // }

    // public void view(){

    // }

    
}
