package tdd.exo1.service.book;

import tdd.exo1.entity.Book;
import tdd.exo1.exception.ObjectNotFoundException;

public class BookInfoService {
    private final BookDataDBService dbDataService;

    private final BookDataWebService webDataService;

    private final BookDBService bookDBService;

    public Book getBookInfo(String isbn) throws ObjectNotFoundException {
        Book b = dbDataService.fetch(isbn);

        if (b == null)
            b = webDataService.fetch(isbn);

        if (b == null)
            throw new ObjectNotFoundException("No book found for id " + isbn);

        bookDBService.updateDB(b);
        return b;
    }

    public BookInfoService(BookDataDBService dbDS, BookDataWebService webDS, BookDBService bdbu) {
        this.dbDataService = dbDS;
        this.webDataService = webDS;
        this.bookDBService = bdbu;
    }
}
