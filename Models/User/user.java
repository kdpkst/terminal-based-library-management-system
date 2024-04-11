package Models.User;

import java.util.List;

import Models.Book.book;
import Models.BookCopy.bookCopy;

public interface user {

    /**
     * @param username Username entered when the user login
     * @param password Password entered when the user login
     * @return An integer indicating the login status:
     *         1 if the login is successful
     *         0 if the username and password do not match or the user type is not 0
     *         -1 if there are multiple user records with the same username
     */
    public int login(String username, String password);
    public List<book> viewAllBooks();
    public List<bookCopy> viewAllCopiesforOneBook(int bid);
    
    /**
     * @param searchKey   The attribute of the book to search by (e.g., "author", "genre", "title").
     * @param searchValue The value to search for within the specified attribute.
     * @return A list of books matching the search criteria; null if the search key is invalid.
     */
    public List<book> searchBooks(String searchKey, String searchValue);

    //I add these methods in the interface to get attributes of normalUser and easily
    int getUid();
    String getUsername();
    String getPassword();
    int getType();
    String getBidWant();

} 