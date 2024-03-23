package Models;

public class Bookdb {
    private static Bookdb instance;

    private Bookdb(){
    }

    public static Bookdb getInstance() {
        if (instance == null) {
            instance = new Bookdb();
        }
        return instance;
    }
}
