package Models.BookFactory;

import Models.Book.book;

public abstract class bookFactory {

    public abstract book createBook(int bid, String title, String author, String genre, int quantity_available);

}
