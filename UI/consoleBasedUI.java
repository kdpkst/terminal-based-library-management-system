package UI;


import java.util.List;
import java.util.Scanner;

import DatabaseConnection.dbSingleton;
import Factory.UserFactory.managerUserFactory;
import Factory.UserFactory.normalUserFactory;
import Factory.UserFactory.userFactory;
import Models.Book.book;
import Models.BookCopy.bookCopy;
import Models.User.*;

public class consoleBasedUI {

    public void home(){
        System.out.println("\n---Home---");
        System.out.println("1.Login");
        System.out.println("2.Register");
        System.out.println("3.Exit");

        System.out.print("Choose an option(please enter a number): ");
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
                dbSingleton.saveCacheData("./Database/Cache/last_id_map.cache");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option, re-enter number between 1 to 3: ");
                break;
        }

        home();
    }

    public void normalUserInterface(normalUser user){
        System.out.println();
        System.out.println("---!Welcome to the Library!---");
        System.out.println("1.List All Books");
        System.out.println("2.List All Copies for A Book");
        System.out.println("3.Search Book");
        System.out.println("4.Borrow Book");
        System.out.println("5.Return Book");
        System.out.println("6.Back to Home");
        System.out.println("7.Exit");

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
                dbSingleton.saveCacheData("./Database/Cache/last_id_map.cache");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option, re-enter number between 1 to 5: ");
                break;
        }

        normalUserInterface(user);
    }

    public void managerInterface(managerUser user){
        System.out.println();
        System.out.println("---Manager System---");
        System.out.println("1.List All Books");
        System.out.println("2.List All Copies for A Book");
        System.out.println("3.Search Book");
        System.out.println("4.Add Book");
        System.out.println("5.Remove Book");
        System.out.println("6.List All Users");
        System.out.println("7.Back to Home");
        System.out.println("8.Exit");

        System.out.print("Choose an option(please enter a number): ");
        int option = getIntInput();
        switch (option) {
            case 1:

                List<book> books=user.viewAllBooks();
                if(books.size()==0){
                    System.out.println();
                    System.out.println("There's no book in the system, please add a book first !");
                    break;
                }
                System.err.println();
                System.out.printf("%-10s %-30s %-20s %-15s %-10s%n", "Book ID", "Title", "Author", "Genre", "Quantity Available");

                for (book book : books) {
                    
                    System.out.printf("%-10d %-30s %-20s %-15s %-10d%n", 
                                      book.getBid(), 
                                      book.getTitle(), 
                                      book.getAuthor(), 
                                      book.getGenre(), 
                                      book.getQuantityAvailable());
                }
                System.err.println();
                break;

            case 2:
                System.out.println();
                System.out.println("Selected bid:");
                int bid=getIntInput();
                List<bookCopy> copies=user.viewAllCopiesforOneBook(bid);
                System.out.println();
                if(copies.size()==0){
                    System.out.println();
                    System.out.println("There's no matched copies in the System, please list all books and then enter vaild bid !");
                    break;
                }
                System.out.printf("%-5s %-5s %-10s%n", "CID", "BID", "Status");

                
                for (bookCopy copy : copies) {
                    int copy_cid = copy.getCid();
                    int copy_bid = copy.getBid();
                    int copy_status = copy.getStatus();

                    System.out.printf("%-5d %-5d %-10d%n", copy_cid, copy_bid, copy_status);
                }

                System.out.println();
                break;
            case 3:
                //while searching fiels not exists may have some error and exit the programme
                System.out.println("Search fields:");
                String fields=getStringInput();
                System.out.println("Search value:");
                String value=getStringInput();
                List<book> searchedbooks=user.searchBooks(fields, value);
                if(searchedbooks.size()==0){
                    System.out.println();
                    System.out.println("There's no matched book in the system !");
                    break;
                }

                System.err.println();
                System.out.printf("%-10s %-30s %-20s %-15s %-10s%n", "Book ID", "Title", "Author", "Genre", "Quantity Available");

                for (book book : searchedbooks) {
                    
                    System.out.printf("%-10d %-30s %-20s %-15s %-10d%n", 
                                      book.getBid(), 
                                      book.getTitle(), 
                                      book.getAuthor(), 
                                      book.getGenre(), 
                                      book.getQuantityAvailable());
                }
                System.err.println();
                break;

            case 4:
               //there exists some problems in case4
                System.out.println("title::");
                String title=getStringInput();
                System.out.println("author");
                String author=getStringInput();
                System.out.println("genre");
                String genre=getStringInput();
                Boolean outcome= user.addBook(title, author, genre);
                if(outcome){
                    System.out.println("Add book successfully !");
                    break;
                }

                else{
                    System.out.println("Failed to add books, please try again !");
                    break;
                }
                
            case 5:
                //there exists some problems in case 5
                System.out.println("Selected cid:");
                String cid=getStringInput();
                int numcid = Integer.parseInt(cid);
                user.removeBook(numcid);
                break;
            case 6:

                List<user> users=user.viewAllUsers();
                System.out.println();

                System.out.printf("%-5s %-15s %-15s %-5s %-10s%n", "UID", "Username", "Password", "Type", "Bid Want");

                for (user userViewed : users) {

                    System.out.printf("%-5d %-15s %-15s %-5d %-10s%n", 
                                    userViewed.getUid(), 
                                    userViewed.getUsername(), 
                                    userViewed.getPassword(), 
                                    userViewed.getType(), 
                                    userViewed.getBidWant());
                     }

                break;
            case 7:
                
                break;  
            case 8:
                dbSingleton.saveCacheData("./Database/Cache/last_id_map.cache");
                System.exit(0);
                break;                               
            default:
                System.out.println("Invalid option, re-enter number between 1 to 5: ");
                break;
        }
        managerInterface(user);
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
        System.out.println();
        System.out.print("Username: ");
        String username = getStringInput();
        System.out.print("Password: ");
        String password = getStringInput();

        userFactory normalUserFactory = new normalUserFactory();
        normalUser normalUser = (normalUser) normalUserFactory.createUser();

        userFactory managerlUserFactory = new managerUserFactory();
        managerUser managerUser = (managerUser) managerlUserFactory.createUser();
        
        int loginResultNormalUser = normalUser.login(username, password);
        int loginResultmanagerUser = managerUser.login(username, password);

        if(loginResultNormalUser == 1){
            normalUserInterface(normalUser);
        }

        if(loginResultmanagerUser == 1){
            managerInterface(managerUser);
        }

        else{
            System.out.println();
            System.out.println("Login failed, please register or login again !");}

    }

    private void register(){
        System.out.println();
        System.out.print("Enter Username: ");
        String username = getStringInput();
        System.out.print("Enter Password: ");
        String password = getStringInput();

        userFactory normalUserFactory = new normalUserFactory();
        normalUser normalUser = (normalUser) normalUserFactory.createUser();

        int registerResult = normalUser.register(username, password);

        switch (registerResult) {
            case 1:
                // print some prompts message
                normalUserInterface(normalUser);    
                break;
            case 0:
                dbSingleton.saveCacheData("./Database/Cache/last_id_map.cache");
                System.exit(0);
            case -1:
                // print some prompts message
                register();
                break;
        }
    }


}

