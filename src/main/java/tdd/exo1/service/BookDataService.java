package tdd.exo1.service;

import tdd.exo1.entity.Book;

import java.util.ArrayList;
import java.util.List;

public class BookDataService implements DataService<Book>{
    List<Book> books = new ArrayList<>();
    @Override
    public Book fetch(String id) {
        return books.stream().filter(b -> b.getIsbn().equals(id)).findFirst().orElseThrow(null);
    }

    public void add(Book b) {
        books.add(b);
    }

    public void remove(Book b) {
        books.remove(b);
    }
}
