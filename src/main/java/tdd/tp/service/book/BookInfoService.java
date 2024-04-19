package tdd.tp.service.book;

import tdd.tp.entity.Book;
import tdd.tp.exception.BookFormatException;
import tdd.tp.exception.ISBNFormatException;
import tdd.tp.exception.ObjectNotFoundException;
import tdd.tp.service.ISBNService;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class BookInfoService {

    private final ISBNService isbnService;

    private final BookDataDBService bookDataDBService;

    private final BookDataWebService bookDataWebService;

    private final BookDBService bookDBService;


    public BookInfoService(BookDataDBService bookDataDBService, BookDataWebService bookDataWebService, BookDBService bookDBService, ISBNService isbnService) {
        this.bookDataDBService = bookDataDBService;
        this.bookDataWebService = bookDataWebService;
        this.bookDBService = bookDBService;
        this.isbnService = isbnService;
    }

    public Book getBookByID(String isbn) throws ObjectNotFoundException {
        Book b = bookDataDBService.findByID(isbn);

        if (b == null)
            b = bookDataWebService.findByID(isbn);

        if (b == null)
            throw new ObjectNotFoundException("No book found for id " + isbn);

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
    public void updateBook(Book b) throws ObjectNotFoundException, BookFormatException {
        this.getBookByID(b.getIsbn()); // Throw an exception
        List<String> fieldsError = this.isBookInformationFilled(b);

        if (fieldsError.size() > 0) {
            StringBuilder fields = new StringBuilder();
            fieldsError.forEach(f -> fields.append(f).append(' '));
            throw new BookFormatException("field(s) missing : " + fields);
        }

        bookDBService.updateBook(b);
    }

    public Book createBook(Book b) throws ISBNFormatException, BookFormatException {
        if (!this.isbnValid(b.getIsbn()))
            throw new ISBNFormatException("ISBN invalid check sum");

        List<String> bookFieldsInError = this.isBookInformationFilled(b);
        if (bookFieldsInError.size() > 0) {
            Book webDataBook = bookDataWebService.findByID(b.getIsbn());
            try {
                // A TESTER
                for (String fieldNameInError : bookFieldsInError) {
                    Field field = b.getClass().getDeclaredField(fieldNameInError);
                    field.setAccessible(true);
                    Object valueFromWeb = field.get(webDataBook);
                    field.set(b, valueFromWeb);
                }
            } catch (Exception e) {
                throw new BookFormatException(e.getMessage());
            }
        }
        return bookDBService.createBook(b);
    }

    public void deleteBook(String isbn) throws ObjectNotFoundException {
        Book b = this.getBookByID(isbn); // Throw an exception
        bookDBService.deleteBook(b);
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

    public List<String> isBookInformationFilled(Book b) {
        List<String> propertiesNameInError = new ArrayList<>();

        if(b.getAuthor().isEmpty())
            propertiesNameInError.add("author");
        if(b.getIsbn().isEmpty())
            propertiesNameInError.add("isbn");
        if (b.getNbPage() <= 0)
            propertiesNameInError.add("nbPage");
        if (b.getTitle().isEmpty())
            propertiesNameInError.add("title");
        if (b.getEditor().isEmpty())
            propertiesNameInError.add("editor");
        if (b.getSize() == null)
            propertiesNameInError.add("format");

        return propertiesNameInError;
    }

}
