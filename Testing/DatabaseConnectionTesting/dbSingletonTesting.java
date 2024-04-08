package Testing.DatabaseConnectionTesting;

import DatabaseConnection.dbSingleton;

public class dbSingletonTesting {
    public static void main(String[] args) {

        dbSingleton dbConnectionObject = dbSingleton.getInstance();

        // dbConnectionObject.listAll("users");

        // dbConnectionObject.search("users", "username", "y");

        // String[] data = {"2", "siling", "ilovesex"};
        // dbConnectionObject.insert("users", data);

        // dbConnectionObject.delete("users", "uid", "1");

        // String[] data1 = {"8", "master", "wank"};
        // dbConnectionObject.update("users", "password", "master", data1);

    }
}