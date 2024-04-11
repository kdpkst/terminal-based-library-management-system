package Factory.BookFactory;

import Models.Book.book;
import Models.Book.scienceBook;

public class scienceBookFactory extends bookFactory{
    @Override
    public book createBook(int bid, String title, String author, String genre, int quantity_available) {
        return new scienceBook(bid, title, author, genre, quantity_available);
    }   
}
