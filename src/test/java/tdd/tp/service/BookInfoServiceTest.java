package tdd.tp.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import tdd.tp.entity.Book;
import tdd.tp.enums.BookSize;
import tdd.tp.exception.BookFormatException;
import tdd.tp.exception.ISBNFormatException;
import tdd.tp.exception.ObjectNotFoundException;
import tdd.tp.service.book.BookDBService;
import tdd.tp.service.book.BookDataDBService;
import tdd.tp.service.book.BookDataWebService;
import tdd.tp.service.book.BookInfoService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookInfoServiceTest {
    @Mock
    ISBNService isbnService;
    @Mock
    BookDataDBService bookDataDBService;

    @Mock
    BookDataWebService webDataService;

    @Mock
    BookDBService dbService;

    @InjectMocks
    BookInfoService infoService;

    Book bookExample1;

    @BeforeAll
    static void contextLoads() {}

    @BeforeEach
    public void loadBook() {
        bookExample1 = new Book()
                .author("J. R. R. Tolkien")
                .editor("Editor 1")
                .isbn("2080421204")
                .title("Le seigneur des Anneaux")
                .nbPage(1223)
                .size(BookSize.POCHE)
                .isAvailable(true);
    }

    @Test
    public void canGetBookById() throws ObjectNotFoundException {
        when(bookDataDBService.findByID(bookExample1.getIsbn())).thenReturn(bookExample1);

        Book foundBook = infoService.getBookByID(bookExample1.getIsbn());

        assertEquals(bookExample1, foundBook);
    }

    @Test
    public void canGetBooksByTitle() throws ObjectNotFoundException {
        List<Book> books = new ArrayList<>();
        books.add(bookExample1);
        when(bookDataDBService.findAllByTitle(bookExample1.getTitle())).thenReturn(books);

        List<Book> foundBooks = infoService.getBooksByTitle(bookExample1.getTitle());

        assertEquals(books, foundBooks);
    }

    @Test
    public void canGetBooksByAuthor() throws ObjectNotFoundException {
        List<Book> books = new ArrayList<>();
        books.add(bookExample1);
        when(bookDataDBService.findAllByAuthor(bookExample1.getAuthor())).thenReturn(books);

        List<Book> foundBooks = infoService.getBooksByAuthor(bookExample1.getAuthor());

        assertEquals(books, foundBooks);
    }

    @Test
    public void canGetBookWithMissingFromDB() throws ObjectNotFoundException {
        // For the ISBN of the example book, the DB service will return null
        when(bookDataDBService.findByID(bookExample1.getIsbn())).thenReturn(null);

        // For the ISBN of the example book, the WEB service will return the example book object
        when(webDataService.findByID(bookExample1.getIsbn())).thenReturn(bookExample1);

        Book foundBook = infoService.getBookByID(bookExample1.getIsbn());

        assertEquals(bookExample1, foundBook);

    }

    @Test
    public void canGetBookWithDBFirst() throws ObjectNotFoundException {
        // For the ISBN of the example book, the DB service will return the example book object
        when(bookDataDBService.findByID(bookExample1.getIsbn())).thenReturn(bookExample1);

        // For the ISBN of the example book, the WEB service will return the example book object
        when(webDataService.findByID(bookExample1.getIsbn())).thenReturn(bookExample1);

        Book foundBook = infoService.getBookByID(bookExample1.getIsbn());

        assertEquals(bookExample1, foundBook);
        // Verify if the mockWebDataService has been invoked 0 times (So never used)
        verify(webDataService, times(0)).findByID(bookExample1.getIsbn());
        //verifyNoInteractions(mockWEBDataService);
    }

    @Test
    public void cantGetBookWithInvalidISBN() {
        // For the ISBN of the example book, the DB service will return null
        when(bookDataDBService.findByID(bookExample1.getIsbn())).thenThrow(new ObjectNotFoundException(""));

        // For the ISBN of the example book, the WEB service will return null
        when(webDataService.findByID(bookExample1.getIsbn())).thenReturn(null);

        assertThrows(ObjectNotFoundException.class,() -> infoService.getBookByID(bookExample1.getIsbn()));
    }

    @Test
    public void isBookUpdatedInDB() throws ObjectNotFoundException, BookFormatException {
        when(bookDataDBService.findByID(bookExample1.getIsbn())).thenReturn(null);
        when(webDataService.findByID(bookExample1.getIsbn())).thenReturn(bookExample1);

        infoService.updateBook(bookExample1); // Will throw an exception if bookExample1 is invalid

        verify(dbService, times(1)).updateBook(bookExample1);
    }


    @Test
    public void isBookUpdateRejected() {
        Book invalidBook = new Book()
                .author("")
                .editor("Editor 3")
                .isbn("9782290396391")
                .title("Oui")
                .nbPage(123)
                .size(BookSize.POCHE)
                .isAvailable(true);

        when(webDataService.findByID(invalidBook.getIsbn())).thenReturn(invalidBook);

        assertThrows(BookFormatException.class, () -> infoService.updateBook(invalidBook));
        verify(dbService, times(0)).updateBook(invalidBook);
    }


    @Test
    public void createBookSuccessful() throws ISBNFormatException, BookFormatException {
        when(isbnService.ValidatorISBN(bookExample1.getIsbn())).thenReturn(true);

        when(dbService.createBook(bookExample1)).thenReturn(bookExample1);

        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);

        assertEquals(bookExample1, infoService.createBook(bookExample1));
        verify(dbService, times(1)).createBook(bookArgumentCaptor.capture());
        assertEquals(bookExample1.getIsbn(), bookArgumentCaptor.getValue().getIsbn());
        verifyNoInteractions(webDataService);
    }

    @Test
    public void createBookWithInvalidBook() throws ISBNFormatException, BookFormatException {
        Book invalidBook = new Book()
                .author("ALLO")
                .editor("")
                .isbn("9782290396391")
                .title("Non")
                .nbPage(123)
                .size(BookSize.POCHE)
                .isAvailable(true);

        when(isbnService.ValidatorISBN(invalidBook.getIsbn())).thenReturn(true);

        Book invalidBookCompleted = new Book(invalidBook);
        invalidBookCompleted.editor("EA");

        when(webDataService.findByID(invalidBook.getIsbn())).thenReturn(invalidBookCompleted);
        when(dbService.createBook(invalidBook)).thenReturn(invalidBookCompleted);

        assertEquals(invalidBookCompleted, infoService.createBook(invalidBook));
        verify(webDataService, times(1)).findByID(invalidBook.getIsbn());
    }

    @Test
    public void createBookUnsuccessful() throws ISBNFormatException {
        Book invalidBook = new Book()
                .author("ALLO")
                .editor("")
                .isbn("97822903963A")
                .title("Non")
                .nbPage(123)
                .size(BookSize.POCHE)
                .isAvailable(true);

        when(isbnService.ValidatorISBN(invalidBook.getIsbn())).thenReturn(false);

        assertThrows(ISBNFormatException.class, () -> infoService.createBook(invalidBook));
        verify(webDataService, times(0)).findByID(invalidBook.getIsbn());
        verify(dbService, times(0)).createBook(invalidBook);
    }

    @Test
    public void deleteBookWithValidISBN() throws ObjectNotFoundException {
        String isbn = "9782290396391";
        when(bookDataDBService.findByID(isbn)).thenReturn(bookExample1);

        infoService.deleteBook(isbn);
        verify(dbService, times(1)).deleteBook(bookExample1);
    }

    @Test
    public void deleteBookWithInvalidISBN() {
        String isbn = "9782290396391A";

        when(bookDataDBService.findByID(isbn)).thenThrow(new ObjectNotFoundException(""));

        assertThrows(ObjectNotFoundException.class, () -> infoService.deleteBook(isbn));
        verify(dbService, times(0)).deleteBook(any(Book.class));
    }

}