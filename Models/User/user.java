package Models.User;

import java.util.List;

import Models.Book.book;

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
    public List<book> viewAllCopiesforOneBook(int bid);
    // use Strategy pattern to switch between different search mechanism
    // may consider searching by title, author, genre
    public List<book> searchBooks();

} 