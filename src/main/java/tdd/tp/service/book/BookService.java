package tdd.tp.service.book;

import lombok.AllArgsConstructor;
import tdd.tp.entity.Book;
import tdd.tp.exception.BookFormatException;
import tdd.tp.exception.ISBNFormatException;
import tdd.tp.exception.ObjectNotFoundException;
import tdd.tp.service.ISBNService;
import tdd.tp.utils.EntityUtils;

import java.lang.reflect.Field;
import java.util.List;

@AllArgsConstructor
public class BookService {

    private final ISBNService isbnService;

    private final BookDataDBService bookDataDBService;

    private final BookDataWebService bookDataWebService;

    public Book getBookByID(String isbn) throws ObjectNotFoundException {
        Book b = bookDataDBService.findByID(isbn);

        if (b == null)
            b = bookDataWebService.findByID(isbn);

        if (b == null)
            throw new ObjectNotFoundException("Book not found for id " + isbn);

        return b;
    }

    public List<Book> getBooksByTitle(String title) throws ObjectNotFoundException {
        List<Book> books = bookDataDBService.findAllByTitle(title);

        if (books.size() == 0)
            throw new ObjectNotFoundException("No books found for title " + title);

        return books;
    }


    public List<Book> getBooksByAuthor(String author) throws ObjectNotFoundException {
        List<Book> books = bookDataDBService.findAllByAuthor(author);

        if (books.size() == 0)
            throw new ObjectNotFoundException("No books found for author " + author);

        return books;
    }
    public Book updateBook(Book b) throws ObjectNotFoundException, BookFormatException {
        this.getBookByID(b.getIsbn()); // Throw an exception
        List<Field> fieldsError = EntityUtils.containsEmptyFieldsOrNegativeInteger(b);

        if (fieldsError.size() > 0) {
            StringBuilder fields = new StringBuilder();
            fieldsError.forEach(f -> fields.append(f.getName()).append(' '));
            throw new BookFormatException("field(s) missing : " + fields);
        }

        bookDataDBService.updateBook(b);
        return b;
    }

    public Book createBook(Book b) throws ISBNFormatException {
        if (!this.isbnValid(b.getIsbn()))
            throw new ISBNFormatException("ISBN invalid check sum");

        List<Field> bookFieldsInError = EntityUtils.containsEmptyFieldsOrNegativeInteger(b);
        if (bookFieldsInError.size() > 0) {
            Book webDataBook = bookDataWebService.findByID(b.getIsbn());
            EntityUtils.replaceValuesFromFieldName(bookFieldsInError, b, webDataBook);
        }
        return bookDataDBService.createBook(b);
    }

    public void deleteBook(String isbn) throws ObjectNotFoundException {
        Book b = this.getBookByID(isbn); // Throw an exception
        bookDataDBService.deleteBook(b);
    }

    private boolean isbnValid(String isbn) {
        boolean isbnValid;
        try {
            isbnValid = isbnService.ValidatorISBN(isbn);
        } catch (ISBNFormatException e) {
            isbnValid = false;
        }
        return isbnValid;
    }

}
