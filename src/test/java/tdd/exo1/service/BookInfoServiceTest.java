package tdd.exo1.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import tdd.exo1.entity.Book;
import tdd.exo1.exception.ObjectNotFoundException;
import tdd.exo1.mock.MockBookDataService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookInfoServiceTest {

    // DataBase Mock Service
    BookDataService mockDBDataService;

    // Web Mock Service
    BookDataService mockWEBDataService;

    BookDBUpdater mockDBUpdaterService;

    BookInfoService infoService;

    Book bookExample1;

    @BeforeAll
    static void contextLoads() {}

    @BeforeEach
    public void loadBook() {
        mockDBDataService = Mockito.mock(BookDataService.class);
        mockWEBDataService = Mockito.mock(BookDataService.class);
        mockDBUpdaterService = Mockito.mock(BookDBUpdater.class);
        infoService = new BookInfoService(mockDBDataService, mockWEBDataService, mockDBUpdaterService);
        bookExample1 = new Book("2080421204", "Le seigneur des Anneaux", 1223, "J. R. R. Tolkien");
    }

    @Test
    public void canGetBook() throws ObjectNotFoundException {
        when(mockDBDataService.fetch(bookExample1.getIsbn())).thenReturn(bookExample1);

        Book foundBook = infoService.getBookInfo(bookExample1.getIsbn());

        assertEquals(bookExample1, foundBook);
    }

    @Test
    public void canGetBookWithMissingFromDB() throws ObjectNotFoundException {
        // For the ISBN of the example book, the DB service will return null
        when(mockDBDataService.fetch(bookExample1.getIsbn())).thenReturn(null);

        // For the ISBN of the example book, the WEB service will return the example book object
        when(mockWEBDataService.fetch(bookExample1.getIsbn())).thenReturn(bookExample1);

        Book foundBook = infoService.getBookInfo(bookExample1.getIsbn());

        assertEquals(bookExample1, foundBook);

    }

    @Test
    public void canGetBookWithDBFirst() throws ObjectNotFoundException {
        // For the ISBN of the example book, the DB service will return the example book object
        when(mockDBDataService.fetch(bookExample1.getIsbn())).thenReturn(bookExample1);

        // For the ISBN of the example book, the WEB service will return the example book object
        when(mockWEBDataService.fetch(bookExample1.getIsbn())).thenReturn(bookExample1);

        Book foundBook = infoService.getBookInfo(bookExample1.getIsbn());

        assertEquals(bookExample1, foundBook);
        // Verify if the mockWebDataService has been invoked 0 times (So never used)
        verify(mockWEBDataService, times(0)).fetch(bookExample1.getIsbn());
        //verifyNoInteractions(mockWEBDataService);
    }

    @Test
    public void cantGetBookWithInvalidISBN() {
        // For the ISBN of the example book, the DB service will return null
        when(mockDBDataService.fetch(bookExample1.getIsbn())).thenThrow(new ObjectNotFoundException(""));

        // For the ISBN of the example book, the WEB service will return null
        when(mockWEBDataService.fetch(bookExample1.getIsbn())).thenReturn(null);

        assertThrows(ObjectNotFoundException.class,() -> infoService.getBookInfo(bookExample1.getIsbn()));
    }

    @Test
    public void isBookUpdatedInDB() throws ObjectNotFoundException {
        when(mockDBUpdaterService.updateDB(bookExample1)).thenReturn(true);

        // For the ISBN of the example book, the DB service will return null
        when(mockDBDataService.fetch(bookExample1.getIsbn())).thenReturn(null);

        // For the ISBN of the example book, the WEB service will return the example book object
        when(mockWEBDataService.fetch(bookExample1.getIsbn())).thenReturn(bookExample1);

        Book foundBook = infoService.getBookInfo(bookExample1.getIsbn());

        verify(mockDBUpdaterService).updateDB(bookExample1);
    }

    @Test
    public void testCreateBook() {
        when(mockDBUpdaterService.updateDB(bookExample1)).thenReturn(true);

        when(mockDBDataService.fetch(bookExample1.getIsbn())).thenReturn(bookExample1);

        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(mockDBUpdaterService).updateDB(bookArgumentCaptor.capture());

        assertEquals(bookExample1.getIsbn(), bookArgumentCaptor.getValue().getIsbn());
    }
}