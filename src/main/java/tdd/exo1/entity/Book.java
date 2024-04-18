package tdd.exo1.entity;

public class Book {
    private final String isbn;
    private final String title;
    private final int nbPage;
    private final String author;

    public Book(String i, String t, int n, String a) {
        this.isbn = i;
        this.title = t;
        this.nbPage = n;
        this.author = a;
    }
    public String getTitle() {
        return title;
    }
    public int getNbPage() {
        return nbPage;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }
}
