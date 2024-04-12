package Testing.UserTesting;

import java.util.HashMap;

import DatabaseConnection.dbSingleton;
import Models.User.managerUser;

public class normalUserTesting {
    public static void main(String[] args) {
        dbSingleton.initializeLastIdMap("./Database/Cache/last_id_map.cache");
        managerUser user=new managerUser();
        // user.removeBook(2);
        // user.addBook("A", "B", "science");
        System.out.println(user.searchBooks("author", "iii"));
    }   
}
