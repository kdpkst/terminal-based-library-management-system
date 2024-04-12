package UI;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
        System.out.println("6.Subscribe Book Wanted");
        System.out.println("7.Back to Home (logout)");
        System.out.println("8.Exit");

        System.out.print("Choose an option: ");
        int option = getIntInput();
        switch (option) {
            case 1:
                listAllBook(user);
                break;
            case 2:
                listAllCopiesForOneBook(user);
                break;
            case 3:
                searchBooks(user);
                break;
            case 4:
                borrowBook(user);
                break;
            case 5:
                returnBook(user);
                break;
            case 6:
                subscribeBook(user);
                break;
            case 7:
                home();
                break;               
            case 8:
                dbSingleton.saveCacheData("./Database/Cache/last_id_map.cache");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option, re-enter number between 1 to 8");
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
        System.out.println("7.Back to Home (logout)");
        System.out.println("8.Exit");

        System.out.print("Choose an option(please enter a number): ");
        int option = getIntInput();
        switch (option) {
            case 1:
                List<book> books=user.viewAllBooks();
                if(books.size()==0){
                    System.out.println();
                    System.out.println("There's no book in the system, please add a book first!");
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
                String key;
                List<String> validInputs = Arrays.asList("science", "business", "novel", "history");

                while (true) {
                    System.out.println("Search value (title, ):");
                    key = getStringInput();

                    if (validInputs.contains(key)) {
                        break;
                    } else {
                        System.out.println("Invalid input. Please enter one of the following values: science, business, novel, history");
                    }
                }
                System.out.println("Search value:");
                String value=getStringInput();
                List<book> searchedbooks=user.searchBooks(key, value);
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
                home();
                break;  
            case 8:
                dbSingleton.saveCacheData("./Database/Cache/last_id_map.cache");
                System.exit(0);
                break;                               
            default:
                System.out.println("Invalid option, re-enter number between 1 to 8");
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
            subscriptionNotifier(normalUser);
            normalUserInterface(normalUser);
        }

        if(loginResultmanagerUser == 1){
            managerInterface(managerUser);
        }

        else{
            System.out.println();
            System.out.println("Login failed, please register or login again !");
        }

       
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
                System.out.println("Successfully registered!");
                System.out.println("The system has helped you automatically login!");
                normalUserInterface(normalUser);    
                break;
            case 0:
                System.out.println("Unknown I/O error occurred!");
                dbSingleton.saveCacheData("./Database/Cache/last_id_map.cache");
                System.exit(0);
            case -1:
                System.out.println("Duplicate username, please change a username!");
                register();
                break;
        }
    }

    // Shuchen Yuan 4.12
    // if the user want to see all the books 
    private void listAllBook(normalUser user){
        // test if the user is null
        if (user == null) {
            System.out.println("The user is null");
        }
        List<book> books= user.viewAllBooks();
        if(books.size()==0){
            System.out.println();
            System.out.println("There's no book in the system, please add a book first !");
            return; // stop running the following codes
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
        return;
    }

    private void listAllCopiesForOneBook(normalUser user){
        System.out.println();
        System.out.println("Selected bid:");
        int bid = getIntInput();
        List<bookCopy> copies=user.viewAllCopiesforOneBook(bid);
        System.out.println();
        if(copies.size() == 0){
            System.out.println();
            System.out.println("There's no matched copies in the System, please list all books and then enter vaild bid!");
            return;
        }
        System.out.printf("%-5s %-5s %-10s%n", "CID", "BID", "Status");

        for (bookCopy copy : copies) {
            int copy_cid = copy.getCid();
            int copy_bid = copy.getBid();
            int copy_status = copy.getStatus();

            System.out.printf("%-5d %-5d %-10d%n", copy_cid, copy_bid, copy_status);
        }
        System.out.println();
        return;
    }

    private void searchBooks(normalUser user){
        //while searching fiels not exists may have some error and exit the programme
        System.out.println("Search fields:");
        String fields=getStringInput();
        System.out.println("Search value:");
        String value=getStringInput();
        List<book> searchedbooks=user.searchBooks(fields, value);
        if(searchedbooks.size()==0){
            System.out.println();
            System.out.println("There's no matched book in the system !");
            return;
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
        return;
    }

    private void borrowBook(normalUser user){
        System.out.println();
        System.out.print("Please enter the CID of the book you want to borrow: ");
        int inputCid = Integer.parseInt(getStringInput());
        int borrowResult = user.borrowBook(inputCid);
        switch (borrowResult) {
            case 1:
                System.out.println("Successfully borrowed this book (Please return it in time) !");
                break;
            case 0:
                System.out.println("Input CID does not exist!");
                break;
            case -1:
                System.out.println("Current book copy has been borrowed!");
                break;
        }
    }

    private void returnBook(normalUser user){
        System.out.println();
        System.out.print("Please enter the CID of the book for return: ");
        int inputCid = Integer.parseInt(getStringInput());
        try{
            double amount = user.returnBook(inputCid);
            if (amount < 0.0){
                System.out.println("Please check the correctness of your input CID");
            }
            if (amount == 0.0){
                System.out.println("Book has successfully returned!");
            }
            if (amount > 0.0) {
                System.out.println("Overdue Book Return! You are fined $" + amount);
            }
        }
        catch (Exception e) {
            System.out.println("Unknown error occurred!");
        }
    }

    private void subscribeBook(normalUser user){
        System.out.println();
        System.out.print("Please enter the BID of the book you want to subscribe: ");
        int inputBid = Integer.parseInt(getStringInput());
        int subscribeResult = user.subscribe(inputBid);
        switch (subscribeResult) {
            case 1:
                System.out.println("Successfully subscribed this book!");
                break;
            case 0:
                System.out.println("This book is currently available, you can borrow it!");
                break;
            case -1:
                System.out.println("This book does not exist!");
                break;
        }
    }

    private void subscriptionNotifier(normalUser user){
        dbSingleton dbConnctor = dbSingleton.getInstance();
        String bidWantString = user.getBidWant();
        String[] bidWantArray = bidWantString.split("-");
        List<String> temp = new ArrayList<>();

        if (!bidWantString.equals("none")){
           
            for (int i = 0; i < bidWantArray.length; i++){
                List<Map<String, String>> booksRecordList = dbConnctor.preciseSearch("books", "bid", bidWantArray[i]);
                if (booksRecordList.size() == 1){
                    Map<String, String> bookRecord = booksRecordList.get(0);
                    if (Integer.parseInt(bookRecord.get("quantity_available")) > 0){
                        temp.add(bidWantArray[i]);
                    }
                }
            }
            
            if (temp.size() > 0){
                System.out.println();
                System.out.println("-----------Subscription Notifcation------------");
                System.out.print("!!!Book(s) with ID ");
                for (int j = 0; j < temp.size(); j++) {
                    System.out.print(bidWantArray[j] + " ");
                }
                System.out.print("is(are) available right now!!!");
                System.out.println();
                System.out.println("-----------Subscription Notifcation------------");
                System.out.println();
            }

        }
    }

}

