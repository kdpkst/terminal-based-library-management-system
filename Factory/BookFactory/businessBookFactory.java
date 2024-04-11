package Factory.BookFactory;

import Models.Book.book;
import Models.Book.businessBook;

public class businessBookFactory extends bookFactory{
    @Override
    public book createBook(int bid, String title, String author, String genre, int quantity_available) {
        return new businessBook(bid, title, author, genre, quantity_available);
    }   
}
