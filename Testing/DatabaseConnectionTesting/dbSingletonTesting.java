package Testing.DatabaseConnectionTesting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import DatabaseConnection.dbSingleton;

public class dbSingletonTesting {
    public static void main(String[] args) {

        dbSingleton dbConnector = dbSingleton.getInstance();

        // List<Map<String, String>> result = dbConnector.listAll("users");
        // System.out.println(result);

        // List<Map<String, String>> result1 = dbConnector.fuzzySearch("users", "username", "y");
        // System.out.println(result1);

        // List<Map<String, String>> result2 = dbConnector.preciseSearch("users", "username", "strong");
        // System.out.println(result2);

        // String[] data = {"2", "siling", "ilovesex"};
        // dbConnector.insert("users", data);

        // dbConnector.delete("users", "uid", "1");

        // String[] data1 = {"8", "master", "wank"};
        // dbConnector.update("users", "password", "master", data1);

    }
}