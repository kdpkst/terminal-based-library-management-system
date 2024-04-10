package Models.User;

import java.util.List;

import Models.Book.book;

public interface user {

    /**
     * @param username: input username
     * @param password: input password
     * @return 1: successfully login; 0:input password incorrect or wrong user type; -1:username does not exist
     */
    public int login(String username, String password);
    public List<book> viewAllBooks();
    public List<book> viewAllCopiesforOneBook(int bid);
    // use Strategy pattern to switch between different search mechanism
    // may consider searching by title, author, genre
    public List<book> searchBooks();

} 