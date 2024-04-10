package Testing.DatabaseConnectionTesting;

import java.util.ArrayList;
import java.util.HashMap;
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

        List<Map<String, String>> data = new ArrayList<Map<String,String>>();
        Map<String, String> map1 = new HashMap<>();
        map1.put("cid", "1");
        map1.put("bid", "1");
        map1.put("status", "2");
        data.add(map1);
        Map<String, String> map2 = new HashMap<>();
        map2.put("cid", "3");
        map2.put("bid", "4");
        map2.put("status", "9");
        data.add(map2);

        Map<String, String> map3 = new HashMap<>();
        map3.put("cid", "09");
        map3.put("bid", "33");
        map3.put("status", "af");
        data.add(map3);
        
        dbConnector.insert("book_copies", data);

        // dbConnector.delete("users", "uid", "1");

        // String[] data1 = {"8", "master", "wank"};
        // dbConnector.update("users", "password", "master", data1);

    }
}