package tdd.tp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import tdd.tp.entity.Book;
import tdd.tp.entity.Booking;
import tdd.tp.entity.Subscriber;
import tdd.tp.enums.BookSizeEnum;
import tdd.tp.enums.BookingStatusEnum;
import tdd.tp.enums.GenderEnum;
import tdd.tp.exception.BookingFormatException;
import tdd.tp.exception.ObjectNotFoundException;
import tdd.tp.exception.TooManyBookingsOpenException;
import tdd.tp.service.book.BookService;
import tdd.tp.service.booking.BookingDataDBService;
import tdd.tp.service.booking.BookingService;
import tdd.tp.service.subscriber.SubscriberService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookingServiceTest {
    @InjectMocks
    BookingService bookingService;
    @Mock
    BookingDataDBService bookingDataDBService;
    @Mock
    SubscriberService subscriberService;
    @Mock
    BookService bookService;
    @Mock
    MailService mailService;

    Book book1;
    Subscriber subscriber1;
    Booking booking1;
    @BeforeEach
    public void loadContext() {
        book1 = Book.builder()
                .author("J. R. R. Tolkien")
                .editor("Editor 1")
                .isbn("2080421204")
                .title("Le seigneur des Anneaux")
                .nbPage(1223)
                .size(BookSizeEnum.POCHE)
                .isAvailable(true)
                .build();
        subscriber1 = Subscriber.builder()
                .mail("gerard@gmail.com")
                .firstName("Gerard")
                .name("Hernandez")
                .birthdate(LocalDate.now())
                .gender(GenderEnum.MALE)
                .code("AZERTY")
                .build();
        booking1 = Booking.builder()
                .id("B1")
                .status(BookingStatusEnum.OPEN)
                .entryDate(LocalDate.now())
                .limitDate(LocalDate.now().plusMonths(4))
                .book(book1)
                .subscriber(subscriber1)
                .build();
    }

    @Test
    void createBookingValid() throws ObjectNotFoundException, TooManyBookingsOpenException {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking1);

        when(subscriberService.getSubscriberById(subscriber1.getCode())).thenReturn(subscriber1);
        when(bookService.getBookByID(book1.getIsbn())).thenReturn(book1);
        when(bookingDataDBService.findAllBySubscriber(subscriber1.getCode())).thenReturn(bookings);

        Booking bookingCreated = bookingService.createBooking(subscriber1.getCode(), book1.getIsbn());

        verify(bookingDataDBService, times(1)).createBooking(any(Booking.class));
        assertEquals(booking1.getBook(), bookingCreated.getBook());
        assertEquals(booking1.getSubscriber(), bookingCreated.getSubscriber());
        assertEquals(bookingCreated.getEntryDate().plusMonths(4), bookingCreated.getLimitDate());
    }

    @Test
    void createBookingButTooManyBooking() throws ObjectNotFoundException {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking1);
        bookings.add(booking1);
        bookings.add(booking1);

        when(subscriberService.getSubscriberById(subscriber1.getCode())).thenReturn(subscriber1);
        when(bookService.getBookByID(book1.getIsbn())).thenReturn(book1);
        when(bookingDataDBService.findAllBySubscriber(subscriber1.getCode())).thenReturn(bookings);

        assertThrows(TooManyBookingsOpenException.class, () -> bookingService.createBooking(subscriber1.getCode(), book1.getIsbn()));
        verify(bookingDataDBService, times(0)).createBooking(any(Booking.class));
    }

    @Test
    void updateBookingValid() throws ObjectNotFoundException, TooManyBookingsOpenException, BookingFormatException {
        when(bookingDataDBService.findByID(booking1.getId())).thenReturn(booking1);
        when(bookingDataDBService.findAllBySubscriber(subscriber1.getCode())).thenReturn(new ArrayList<>());

        Booking bookingUpdated = bookingService.updateBooking(booking1);

        verify(bookingDataDBService, times(1)).updateBooking(any(Booking.class));
        assertEquals(booking1.getBook(), bookingUpdated.getBook());
        assertEquals(booking1.getSubscriber(), bookingUpdated.getSubscriber());
    }

    @Test
    void updateBookingWithTooManyBooking() {
        Booking booking2 = Booking.builder()
                .book(book1)
                .subscriber(subscriber1)
                .status(BookingStatusEnum.OPEN)
                .id("B2")
                .build();

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking2);
        bookings.add(booking2);
        bookings.add(booking2);

        when(bookingDataDBService.findByID(booking1.getId())).thenReturn(booking1);
        when(bookingDataDBService.findAllBySubscriber(subscriber1.getCode())).thenReturn(bookings);

        assertThrows(TooManyBookingsOpenException.class, () -> bookingService.updateBooking(booking1));
        verify(bookingDataDBService, times(0)).updateBooking(any(Booking.class));
    }

    @Test
    void updateBookingWithAlmostTooManyBooking() throws BookingFormatException, ObjectNotFoundException, TooManyBookingsOpenException {
        Booking booking2 = Booking.builder()
                .book(book1)
                .subscriber(subscriber1)
                .status(BookingStatusEnum.OPEN)
                .id("B2")
                .build();

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking2);
        bookings.add(booking2);
        bookings.add(booking1);

        when(bookingDataDBService.findByID(booking1.getId())).thenReturn(booking1);
        when(bookingDataDBService.findAllBySubscriber(subscriber1.getCode())).thenReturn(bookings);

        Booking bookingUpdated = bookingService.updateBooking(booking1);

        verify(bookingDataDBService, times(1)).updateBooking(any(Booking.class));
        assertEquals(booking1.getBook(), bookingUpdated.getBook());
        assertEquals(booking1.getSubscriber(), bookingUpdated.getSubscriber());
    }

    @Test
    void closeBookingValid() throws ObjectNotFoundException {
        when(bookingDataDBService.findByID(booking1.getId())).thenReturn(booking1);

        bookingService.closeBooking(booking1.getId(), true);

        verify(bookingDataDBService, times(1)).updateBooking(booking1);
    }

    @Test
    void closeBookingButNotFound() {
        when(bookingDataDBService.findByID(booking1.getId())).thenThrow(ObjectNotFoundException.class);

        assertThrows(ObjectNotFoundException.class, () -> bookingService.closeBooking(booking1.getId(), true));
        verify(bookingDataDBService, times(0)).updateBooking(booking1);
    }

    @Test
    void getOpenBookings() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking1);

        when(bookingDataDBService.findAllByStatus(BookingStatusEnum.OPEN)).thenReturn(bookings);

        assertEquals(bookings, bookingService.getAllBookingByStatus(BookingStatusEnum.OPEN));
    }

    @Test
    void getBookingsBySubscriber() {
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking1);

        when(bookingDataDBService.findAllBySubscriber(subscriber1.getCode())).thenReturn(bookings);

        assertEquals(bookings, bookingService.getAllBookingBySubscriber(subscriber1.getCode()));
    }
    @Test
    void mailExpiredBookingsBySubscriberWith2Subscribers() {
        Subscriber subscriber2 = Subscriber.builder()
                .code("S2")
                .build();

        Booking booking2 = Booking.builder()
                .book(book1)
                .subscriber(subscriber2)
                .id("B2")
                .build();

        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking1);
        bookings.add(booking2);

        when(bookingDataDBService.findAllByStatus(BookingStatusEnum.MANUALLY_CLOSED)).thenReturn(new ArrayList<>());
        when(bookingDataDBService.findAllByStatus(BookingStatusEnum.EXPIRED)).thenReturn(bookings);

        bookingService.expirationMail();

        // 2 different subscriber for 3 bookings so only 2 mail should be sent
        verify(mailService, times(2)).sendSimpleMessage(any(), any(), any());
    }

    @Test
    void mailExpiredBookingsBySubscriberWithoutAnyExpiredBooking() {
        when(bookingDataDBService.findAllByStatus(BookingStatusEnum.MANUALLY_CLOSED)).thenReturn(new ArrayList<>());
        when(bookingDataDBService.findAllByStatus(BookingStatusEnum.EXPIRED)).thenReturn(new ArrayList<>());

        bookingService.expirationMail();

        // No booking expired / closed so no mail should be sent
        verify(mailService, times(0)).sendSimpleMessage(any(), any(), any());
    }

}