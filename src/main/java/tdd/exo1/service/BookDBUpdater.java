package tdd.exo1.service;

import tdd.exo1.entity.Book;

public class BookDBUpdater {

    public boolean updateDB(Book b) {
        throw new RuntimeException("Not Implemented yet");
    }

    public void createDB(String isbn, Book b) {
        this.updateDB(b);
        throw new RuntimeException("Not implemented yet");
    }
}
