package Models.User;

import java.util.List;

import Models.Book.book;

public interface user {

    public boolean login(String username, String password);
    public List<book> viewAllBooks();
    public List<book> viewAllBookCopies(int bid);
    // use Strategy pattern to switch between different search mechanism
    // may consider searching by title, author, genre
    public List<book> searchBooks();

} 