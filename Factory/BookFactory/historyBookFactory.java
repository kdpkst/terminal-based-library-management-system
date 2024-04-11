package Factory.BookFactory;

import Models.Book.book;
import Models.Book.historyBook;

public class historyBookFactory extends bookFactory{
    @Override
    public book createBook(int bid, String title, String author, String genre, int quantity_available) {
        return new historyBook(bid, title, author, genre, quantity_available);
    }   
}
