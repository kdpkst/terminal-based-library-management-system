package Models.BookFactory;

import Models.Book.book;
import Models.Book.novelBook;

public class novelBookFactory extends bookFactory{
    @Override
    public book createSpecificBook(int bid, String title, String author, String genre, int quantity_available) {
        return new novelBook(bid, title, author, genre, quantity_available);
    }   
}
