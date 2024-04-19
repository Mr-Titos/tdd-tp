package tdd.tp.service.book;

import tdd.tp.entity.Book;
import tdd.tp.service.DataService;

import java.util.ArrayList;
import java.util.List;

public class BookDataWebService implements DataService<Book> {
    List<Book> books = new ArrayList<>();

    private static BookDataWebService session = null;

    private BookDataWebService() {}

    public static BookDataWebService getSession() {
        if(session == null)
            session = new BookDataWebService();
        return session;
    }
    @Override
    public Book findByID(String id) {
        return books.stream().filter(b -> b.getIsbn().equals(id)).findFirst().orElseThrow(null);
    }

    public List<Book> findAllByTitle(String title) {
        return books.stream().filter(b -> b.getTitle().equals(title)).toList();
    }

    public List<Book> findAllByAuthor(String author) {
        return books.stream().filter(b -> b.getAuthor().equals(author)).toList();
    }
}
