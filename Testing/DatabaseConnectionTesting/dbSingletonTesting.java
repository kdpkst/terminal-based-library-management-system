package Testing.DatabaseConnectionTesting;

import DatabaseConnection.dbSingleton;

public class dbSingletonTesting {
    public static void main(String[] args) {

        dbSingleton dbConnector = dbSingleton.getInstance();

        // dbConnector.listAll("users");

        // dbConnector.search("users", "username", "y");

        // String[] data = {"2", "siling", "ilovesex"};
        // dbConnector.insert("users", data);

        // dbConnector.delete("users", "uid", "1");

        // String[] data1 = {"8", "master", "wank"};
        // dbConnector.update("users", "password", "master", data1);

    }
}