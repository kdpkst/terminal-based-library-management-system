package Models.BookFactory;

import Models.Book.book;

public abstract class bookFactory {

    public abstract book createSpecificBook(int bid, String title, String author, String genre, int quantity_available);

    public book createBook(int bid, String title, String author, String genre, int quantity_available){
        switch (genre) {
            case "science":
                scienceBookFactory sBookFactory = new scienceBookFactory();
                sBookFactory.createBook(bid, title, author, genre, quantity_available);
            case "novel":
                novelBookFactory nBookFactory = new novelBookFactory();
                nBookFactory.createBook(bid, title, author, genre, quantity_available);
            case "business":
                businessBookFactory bBokkBookFactory = new businessBookFactory();
                bBokkBookFactory.createBook(bid, title, author, genre, quantity_available);
            case "history":
                historyBookFactory hBookFactory = new historyBookFactory();
                hBookFactory.createBook(bid, title, author, genre, quantity_available);
            default:
                return null;
        }
    }

}
