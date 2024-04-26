package tdd.tp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import tdd.tp.entity.Book;
import tdd.tp.enums.BookSizeEnum;
import tdd.tp.exception.BookFormatException;
import tdd.tp.exception.ISBNFormatException;
import tdd.tp.exception.ObjectNotFoundException;
import tdd.tp.service.book.BookDataDBService;
import tdd.tp.service.book.BookDataWebService;
import tdd.tp.service.book.BookService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookServiceTest {
    @Mock
    ISBNService isbnService;
    @Mock
    BookDataDBService dbDataService;
    @Mock
    BookDataWebService webDataService;
    @InjectMocks
    BookService infoService;
    Book bookExample1;

    @BeforeEach
    public void loadContext() {
        bookExample1 = Book.builder()
                .author("J. R. R. Tolkien")
                .editor("Editor 1")
                .isbn("2080421204")
                .title("Le seigneur des Anneaux")
                .nbPage(1223)
                .size(BookSizeEnum.POCHE)
                .isAvailable(true)
                .build();
    }

    @Test
    public void canGetBookById() throws ObjectNotFoundException {
        when(dbDataService.findByID(bookExample1.getIsbn())).thenReturn(bookExample1);

        Book foundBook = infoService.getBookByID(bookExample1.getIsbn());

        assertEquals(bookExample1, foundBook);
    }

    @Test
    public void canGetBooksByTitle() throws ObjectNotFoundException {
        List<Book> books = new ArrayList<>();
        books.add(bookExample1);
        when(dbDataService.findAllByTitle(bookExample1.getTitle())).thenReturn(books);

        List<Book> foundBooks = infoService.getBooksByTitle(bookExample1.getTitle());

        assertEquals(books, foundBooks);
    }

    @Test
    public void canGetBooksByAuthor() throws ObjectNotFoundException {
        List<Book> books = new ArrayList<>();
        books.add(bookExample1);
        when(dbDataService.findAllByAuthor(bookExample1.getAuthor())).thenReturn(books);

        List<Book> foundBooks = infoService.getBooksByAuthor(bookExample1.getAuthor());

        assertEquals(books, foundBooks);
    }

    @Test
    public void canGetBookWithMissingFromDB() throws ObjectNotFoundException {
        // For the ISBN of the example book, the DB service will return null
        when(dbDataService.findByID(bookExample1.getIsbn())).thenReturn(null);

        // For the ISBN of the example book, the WEB service will return the example book object
        when(webDataService.findByID(bookExample1.getIsbn())).thenReturn(bookExample1);

        Book foundBook = infoService.getBookByID(bookExample1.getIsbn());

        assertEquals(bookExample1, foundBook);

    }

    @Test
    public void canGetBookWithDBFirst() throws ObjectNotFoundException {
        // For the ISBN of the example book, the DB service will return the example book object
        when(dbDataService.findByID(bookExample1.getIsbn())).thenReturn(bookExample1);

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
        when(dbDataService.findByID(bookExample1.getIsbn())).thenThrow(new ObjectNotFoundException(""));

        // For the ISBN of the example book, the WEB service will return null
        when(webDataService.findByID(bookExample1.getIsbn())).thenReturn(null);

        assertThrows(ObjectNotFoundException.class,() -> infoService.getBookByID(bookExample1.getIsbn()));
    }

    @Test
    public void isBookUpdatedInDB() throws ObjectNotFoundException, BookFormatException {
        when(dbDataService.findByID(bookExample1.getIsbn())).thenReturn(null);
        when(webDataService.findByID(bookExample1.getIsbn())).thenReturn(bookExample1);

        infoService.updateBook(bookExample1); // Will throw an exception if bookExample1 is invalid

        verify(dbDataService, times(1)).updateBook(bookExample1);
    }


    @Test
    public void isBookUpdateRejected() {
        Book invalidBook = Book.builder()
                .author("")
                .editor("Editor 3")
                .isbn("9782290396391")
                .title("Oui")
                .nbPage(123)
                .size(BookSizeEnum.POCHE)
                .isAvailable(true)
                .build();

        when(webDataService.findByID(invalidBook.getIsbn())).thenReturn(invalidBook);

        assertThrows(BookFormatException.class, () -> infoService.updateBook(invalidBook));
        verify(dbDataService, times(0)).updateBook(invalidBook);
    }


    @Test
    public void createBookSuccessful() throws ISBNFormatException, BookFormatException {
        when(isbnService.ValidatorISBN(bookExample1.getIsbn())).thenReturn(true);

        when(dbDataService.createBook(bookExample1)).thenReturn(bookExample1);

        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);

        assertEquals(bookExample1, infoService.createBook(bookExample1));
        verify(dbDataService, times(1)).createBook(bookArgumentCaptor.capture());
        assertEquals(bookExample1.getIsbn(), bookArgumentCaptor.getValue().getIsbn());
        verifyNoInteractions(webDataService);
    }

    @Test
    public void createBookWithInvalidBook() throws ISBNFormatException, BookFormatException {
        Book invalidBook = Book.builder()
                .author("ALLO")
                .editor("")
                .isbn("9782290396391")
                .title("Non")
                .nbPage(123)
                .size(BookSizeEnum.POCHE)
                .isAvailable(true)
                .build();

        when(isbnService.ValidatorISBN(invalidBook.getIsbn())).thenReturn(true);

        Book invalidBookCompleted = new Book(invalidBook);
        invalidBookCompleted.setEditor("EA");

        when(webDataService.findByID(invalidBook.getIsbn())).thenReturn(invalidBookCompleted);
        when(dbDataService.createBook(invalidBook)).thenReturn(invalidBookCompleted);

        assertEquals(invalidBookCompleted, infoService.createBook(invalidBook));
        verify(webDataService, times(1)).findByID(invalidBook.getIsbn());
    }

    @Test
    public void createBookUnsuccessful() throws ISBNFormatException {
        Book invalidBook = Book.builder()
                .author("ALLO")
                .editor("")
                .isbn("97822903963A")
                .title("Non")
                .nbPage(123)
                .size(BookSizeEnum.POCHE)
                .isAvailable(true)
                .build();

        when(isbnService.ValidatorISBN(invalidBook.getIsbn())).thenReturn(false);

        assertThrows(ISBNFormatException.class, () -> infoService.createBook(invalidBook));
        verify(webDataService, times(0)).findByID(invalidBook.getIsbn());
        verify(dbDataService, times(0)).createBook(invalidBook);
    }

    @Test
    public void deleteBookWithValidISBN() throws ObjectNotFoundException {
        String isbn = "9782290396391";
        when(dbDataService.findByID(isbn)).thenReturn(bookExample1);

        infoService.deleteBook(isbn);
        verify(dbDataService, times(1)).deleteBook(bookExample1);
    }

    @Test
    public void deleteBookWithInvalidISBN() {
        String isbn = "9782290396391A";

        when(dbDataService.findByID(isbn)).thenThrow(new ObjectNotFoundException(""));

        assertThrows(ObjectNotFoundException.class, () -> infoService.deleteBook(isbn));
        verify(dbDataService, times(0)).deleteBook(any(Book.class));
    }

}