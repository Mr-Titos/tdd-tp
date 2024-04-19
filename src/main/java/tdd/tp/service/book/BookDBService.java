package tdd.tp.service.book;

import tdd.tp.entity.Book;
import tdd.tp.repository.BookRepository;

public class BookDBService {

    private BookRepository bookRepository;
    private static BookDBService session = null;

    private BookDBService() {}

    public static BookDBService getSession() {
        if (session == null)
            session = new BookDBService();
        return session;
    }

    public Book updateBook(Book b) {
        throw new RuntimeException("Not Implemented yet");
    }

    public Book createBook(Book b) {
        throw new RuntimeException("Not implemented yet");
    }

    public void deleteBook(Book b) {
        throw new RuntimeException("Not implemented yet");
    }
}
