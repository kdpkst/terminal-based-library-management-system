package Models.Book;

public class scienceBook implements book {
    int bid;
    String title;
    String author;
    String genre;
    int quantityAvailable;

    public scienceBook(int bid, String title, String author, String genre, int quantityAvailable) {
        this.bid = bid;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.quantityAvailable = quantityAvailable;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }
}



 
    

