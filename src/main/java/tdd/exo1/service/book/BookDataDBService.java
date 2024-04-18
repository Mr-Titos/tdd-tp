package tdd.exo1.service.book;

import tdd.exo1.entity.Book;
import tdd.exo1.service.DataService;

public class BookDataDBService implements DataService<Book> {
    @Override
    public Book fetch(String id) {
        throw new RuntimeException("Not Implemented");
    }
}
