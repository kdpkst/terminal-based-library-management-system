package Strategy;

import Models.Book.book;
import java.util.List;

public interface SearchStrategy {
    List<book> search(String criteria);
}
