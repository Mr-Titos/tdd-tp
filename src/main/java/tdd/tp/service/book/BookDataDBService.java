package tdd.tp.service.book;

import tdd.tp.entity.Book;
import tdd.tp.repository.BookRepository;
import tdd.tp.service.DataService;

import java.util.List;

public class BookDataDBService implements DataService<Book> {
    private static BookDataDBService session = null;

    private BookRepository bookRepository;

    private BookDataDBService() {}

    public static BookDataDBService getSession() {
        if (session == null)
            session = new BookDataDBService();
        return session;
    }
    @Override
    public Book findByID(String isbn) {
        Book b = bookRepository.findByIsbn(isbn);
        throw new RuntimeException("Not Implemented");
    }

    public List<Book> findAllByTitle(String title) {
        List<Book> books = bookRepository.findAllByTitle(title);
        throw new RuntimeException("Not Implemented");
    }

    public List<Book> findAllByAuthor(String author) {
        List<Book> books = bookRepository.findAllByAuthor(author);
        throw new RuntimeException("Not Implemented");
    }
}
