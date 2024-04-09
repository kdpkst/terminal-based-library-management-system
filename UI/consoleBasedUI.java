package UI;

import java.util.Scanner;

public class consoleBasedUI {

    public void home(){
        System.out.println("\n---Home---");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");

        System.out.print("Choose an option: ");
        int option = getIntInput();
        switch (option) {
            case 1:
                // if login succeeds, jump to normalUserInterface or managerInterface
                // if login fails, login again
                login();
                break;
            case 2:
                // no duplicate username 
                register();
                break;
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option, re-enter number between 1 to 3: ");
                break;
        }

        home();
    }

    public void normalUserInterface(){
        System.out.println("---!Welcome to the Library!---");
        System.out.println("List All Books");
        System.out.println("List All Copies for A Book");
        System.out.println("Search Book");
        System.out.println("Borrow Book");
        System.out.println("Return Book");
        System.out.println("Back to Home");
        System.out.println("Exit");

        System.out.print("Choose an option: ");
        int option = getIntInput();
        switch (option) {
            case 1:
                
                break;
            case 2:
                
                break;
            case 3:
                
                break;
            case 4:
                
                break;
            case 5:
                
                break;
            case 6:
                
                break;               
            case 7:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option, re-enter number between 1 to 5: ");
                break;
        }

        normalUserInterface();
    }

    public void managerInterface(){
        System.out.println("---Manager System---");
        System.out.println("List All Books");
        System.out.println("List All Copies for A Book");
        System.out.println("Search Book");
        System.out.println("Add Book");
        System.out.println("Remove Book");
        System.out.println("List All Users");
        System.out.println("Search User");
        System.out.println("Back to Home");
        System.out.println("Exit");

        System.out.print("Choose an option: ");
        int option = getIntInput();
        switch (option) {
            case 1:
                
                break;
            case 2:
                
                break;
            case 3:
                
                break;
            case 4:
                
                break;
            case 5:
                
                break;
            case 6:
                
                break;  
            case 7:
                
                break;  
            case 8:
                
                break;                                 
            case 9:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option, re-enter number between 1 to 5: ");
                break;
        }

        managerInterface();
    }

    public static int getIntInput() {        
        Scanner keyboard = new Scanner(System.in);
        String input = keyboard.nextLine();

        int intInput;
        try {
            intInput = Integer.parseInt(input);
        } 
        catch (NumberFormatException e) {
            System.out.print("Please enter a number, re-enter your option: ");
            intInput = getIntInput();
        }
        return intInput;
    }

    public static String getStringInput() {        
        Scanner keyboard = new Scanner(System.in);
        String input = keyboard.nextLine();
        return input;
    }

    private void login(){
        System.out.print("Username: ");
        String username = getStringInput();
        System.out.print("Password: ");
        String password = getStringInput();

    }

    private void register(){


    }


}

