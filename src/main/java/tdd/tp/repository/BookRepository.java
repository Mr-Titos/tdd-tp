package tdd.tp.repository;

import tdd.tp.entity.Book;

import java.util.List;


public interface BookRepository /*extends JpaRepository<Book, String>*/ {
    Book findByIsbn(String isbn);
    List<Book> findAllByTitle(String title);
    List<Book> findAllByAuthor(String author);

}