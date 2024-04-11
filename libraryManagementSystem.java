import DatabaseConnection.dbSingleton;
import UI.consoleBasedUI;

public class libraryManagementSystem {
    public static void main(String[] args) {
        // initialize the lastIdMap when the program starts
        dbSingleton.initializeLastIdMap("./Database/Cache/last_id_map.csv");
        consoleBasedUI ui = new consoleBasedUI();
        ui.home();
    }
    
}
