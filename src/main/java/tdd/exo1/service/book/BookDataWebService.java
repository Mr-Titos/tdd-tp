package tdd.exo1.service.book;

import tdd.exo1.entity.Book;
import tdd.exo1.service.DataService;

import java.util.ArrayList;
import java.util.List;

public class BookDataWebService implements DataService<Book> {
    List<Book> books = new ArrayList<>();
    @Override
    public Book fetch(String id) {
        return books.stream().filter(b -> b.getIsbn().equals(id)).findFirst().orElseThrow(null);
    }
}
