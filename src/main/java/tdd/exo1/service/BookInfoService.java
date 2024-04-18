package tdd.exo1.service;

import tdd.exo1.entity.Book;
import tdd.exo1.exception.ObjectNotFoundException;

public class BookInfoService {
    private final DataService dbDataService;

    private final DataService webDataService;

    private final BookDBUpdater bookDBUpdater;

    public Book getBookInfo(String isbn) throws ObjectNotFoundException {
        Book b = (Book) dbDataService.fetch(isbn);

        if (b == null)
            b = (Book) webDataService.fetch(isbn);

        if (b == null)
            throw new ObjectNotFoundException("No book found for id " + isbn);

        bookDBUpdater.updateDB(b);
        return b;
    }

    public BookInfoService(DataService dbDS, DataService webDS, BookDBUpdater bdbu) {
        this.dbDataService = dbDS;
        this.webDataService = webDS;
        this.bookDBUpdater = bdbu;
    }
}
