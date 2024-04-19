package tdd.tp.entity;

import tdd.tp.enums.BookSize;

public class Book {
    private String isbn;
    private String title;
    private int nbPage = 0;
    private String author;
    private String editor;
    private BookSize size = null;
    private boolean isAvailable = true;

    public Book() {}

    public Book(Book b) {
        this(b.getIsbn(), b.getTitle(), b.getNbPage(), b.getAuthor(), b.getEditor(), b.getSize(), b.isAvailable());
    }

    private Book(String isbn, String title, int nbPage, String author, String editor, BookSize size, boolean isAvailable) {
        isbn(isbn);
        title(title);
        nbPage(nbPage);
        author(author);
        editor(editor);
        size(size);
        isAvailable(isAvailable);
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

    public String getEditor() {
        return editor;
    }

    public BookSize getSize() {
        return size;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
    public Book isbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public Book title(String title) {
        this.title = title;
        return this;
    }

    public Book nbPage(int nbPage) {
        this.nbPage = nbPage;
        return this;
    }

    public Book author(String author) {
        this.author = author;
        return this;
    }

    public Book editor(String editor) {
        this.editor = editor;
        return this;
    }

    public Book isAvailable(boolean available) {
        isAvailable = available;
        return this;
    }

    public Book size(BookSize format) {
        this.size = format;
        return this;
    }
}
