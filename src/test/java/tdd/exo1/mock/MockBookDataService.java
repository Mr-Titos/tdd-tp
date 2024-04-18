package tdd.exo1.mock;

import tdd.exo1.entity.Book;
import tdd.exo1.exception.ObjectNotFoundException;
import tdd.exo1.service.DataService;

import java.util.ArrayList;
import java.util.List;

public class MockBookDataService implements DataService<Book> {
    List<Book> books = new ArrayList<>();
    @Override
    public Book fetch(String id) {
        return null;
    }

    public void add(Book b) {
        books.add(b);
    }

    public void remove(Book b) {
        books.remove(b);
    }

    public void clean() {
        books.clear();
    }
}
