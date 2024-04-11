package Strategy;

import DatabaseConnection.dbSingleton;
import Models.Book.book;
import Factory.BookFactory.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchByGenre implements SearchStrategy {

    @Override
    public List<book> search(String genre){
        dbSingleton dbConnector = dbSingleton.getInstance();
        List<Map<String, String>> SearchResult = dbConnector.fuzzySearch("books", "genre", genre);

        List<book> booksList = new ArrayList<>();
        for (int i = 0; i < SearchResult.size(); i++){
            Map<String, String> bookRecord = SearchResult.get(i);
            int bid = Integer.parseInt(bookRecord.get("bid"));
            String title = bookRecord.get("title");
            String author = bookRecord.get("author");
            genre = bookRecord.get("genre");
            int quantity_available = Integer.parseInt(bookRecord.get("quantity_available"));
            switch(genre){
                case "science":
                    bookFactory sBookFactory = new scienceBookFactory();
                    book scienceBook = sBookFactory.createBook(bid, title, author, genre, quantity_available);
                    booksList.add(scienceBook);
                    break;
                case "business":
                    bookFactory bBookFactory = new businessBookFactory();
                    book businessBook = bBookFactory.createBook(bid, title, author, genre, quantity_available);
                    booksList.add(businessBook);
                    break;
                case "novel":
                    bookFactory nBookFactory = new novelBookFactory();
                    book novelBook = nBookFactory.createBook(bid, title, author, genre, quantity_available);
                    booksList.add(novelBook);
                    break;
                case "history":
                    bookFactory hBookFactory = new historyBookFactory();
                    book historyBook = hBookFactory.createBook(bid, title, author, genre, quantity_available);
                    booksList.add(historyBook);
                    break;
            }
        }

        return booksList;

    }
}
