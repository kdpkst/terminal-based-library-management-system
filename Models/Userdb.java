package Models;

public class Userdb {
    private static Userdb instance;

    private Userdb(){
    }

    public static Userdb getInstance() {
        if (instance == null) {
            instance = new Userdb();
        }
        return instance;
    }


}
