package tdd.tp.service.booking;

import lombok.AllArgsConstructor;
import tdd.tp.entity.Book;
import tdd.tp.entity.Booking;
import tdd.tp.entity.Subscriber;
import tdd.tp.enums.BookingStatusEnum;
import tdd.tp.exception.BookingFormatException;
import tdd.tp.exception.ObjectNotFoundException;
import tdd.tp.exception.TooManyBookingsOpenException;
import tdd.tp.service.MailService;
import tdd.tp.service.book.BookService;
import tdd.tp.service.subscriber.SubscriberService;
import tdd.tp.utils.EntityUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
public class BookingService {
    private final int NB_OPEN_BOOKINGS_BY_USER = 3;

    private final BookingDataDBService bookingDataDBService;
    private final SubscriberService subscriberService;
    private final BookService bookService;
    private final MailService mailService;

    public Booking getBookingById(String id) throws ObjectNotFoundException {
        Booking booking = bookingDataDBService.findByID(id);

        if (booking == null)
            throw new ObjectNotFoundException("Booking not found for id " + id);

        return booking;
    }


    public List<Booking> getAllBookingByStatus(BookingStatusEnum statusEnum) {
        return bookingDataDBService.findAllByStatus(statusEnum);
    }

    public List<Booking> getAllBookingBySubscriber(String subscriberID) {
        return bookingDataDBService.findAllBySubscriber(subscriberID);
    }

    public Booking createBooking(String subscriberId, String bookId) throws ObjectNotFoundException, TooManyBookingsOpenException {
        // Throw ObjectNotFoundException if either is not found
        Subscriber subscriber = subscriberService.getSubscriberById(subscriberId);
        Book book = bookService.getBookByID(bookId);

        // Throw TooManyOpenBookingsException if the user has already more bookings than authorized
        isSubscriberAbleToBook(subscriberId);

        Booking booking = Booking.builder()
                .entryDate(LocalDate.now())
                .limitDate(LocalDate.now().plusMonths(4))
                .status(BookingStatusEnum.OPEN)
                .subscriber(subscriber)
                .book(book)
                .build();

        bookingDataDBService.createBooking(booking);

        return booking;
    }

    public void closeBooking(String id, boolean isManual) throws ObjectNotFoundException {
        Booking booking = this.getBookingById(id);
        booking.setStatus(isManual ? BookingStatusEnum.MANUALLY_CLOSED : BookingStatusEnum.EXPIRED);
        bookingDataDBService.updateBooking(booking);
    }

    public Booking updateBooking(Booking b) throws ObjectNotFoundException, BookingFormatException, TooManyBookingsOpenException {
        this.getBookingById(b.getId()); // Throw an exception

        List<Field> fieldsError = EntityUtils.containsEmptyFieldsOrNegativeInteger(b);
        if (fieldsError.size() > 0) {
            StringBuilder fields = new StringBuilder();
            fieldsError.forEach(f -> fields.append(f.getName()).append(' '));
            throw new BookingFormatException("field(s) missing : " + fields);
        }

        // Throw exception if the limit is hit
        this.isSubscriberAbleToBook(b.getSubscriber().getCode(), b);

        bookingDataDBService.updateBooking(b);
        return b;
    }

    /**
     * Permit to check if the subscriber already have hit its booking limit
     * @param subscriberId
     * @throws TooManyBookingsOpenException
     */
    public void isSubscriberAbleToBook(String subscriberId) throws TooManyBookingsOpenException {
        List<Booking> bookings = bookingDataDBService.findAllBySubscriber(subscriberId)
                .stream().filter(b -> b.getStatus().equals(BookingStatusEnum.OPEN)).toList();
        if (bookings.size() >= NB_OPEN_BOOKINGS_BY_USER)
            throw new TooManyBookingsOpenException(bookings.size() + " bookings already open for user " + subscriberId);
    }

    /**
     * Permit to add a booking in the limit to check if the booking is open
     * @param subscriberId
     * @param booking
     * @throws TooManyBookingsOpenException
     */
    public void isSubscriberAbleToBook(String subscriberId, Booking booking) throws TooManyBookingsOpenException {
        List<Booking> bookings = new java.util.ArrayList<>(bookingDataDBService.findAllBySubscriber(subscriberId)
                .stream().filter(b -> b.getStatus().equals(BookingStatusEnum.OPEN)).toList());

        if (bookings.stream().noneMatch(b -> b.equals(booking))) {
            if (booking.getStatus().equals(BookingStatusEnum.OPEN))
                bookings.add(booking);
        }
        if (bookings.size() > NB_OPEN_BOOKINGS_BY_USER)
            throw new TooManyBookingsOpenException(bookings.size() + " bookings already open for user " + subscriberId);
    }

    public void expirationMail() {
        List<Booking> bookingClosed = bookingDataDBService.findAllByStatus(BookingStatusEnum.EXPIRED);
        bookingClosed.addAll(bookingDataDBService.findAllByStatus(BookingStatusEnum.MANUALLY_CLOSED));

        for (Subscriber subscriber : bookingClosed.stream().map(Booking::getSubscriber).distinct().toList()) {
            List<Booking> bookings = bookingClosed.stream().filter(booking -> booking.getSubscriber().equals(subscriber)).toList();

            StringBuilder stringBuilder = new StringBuilder();
            bookings.forEach(booking ->
                    stringBuilder.append("\n\n")
                    .append("Booking ").append(booking.getId())
                    .append("\nBook : ").append(booking.getBook())
                    .append("\nExpiration Date : ").append(booking.getLimitDate())
            );

            mailService.sendSimpleMessage(
                    subscriber.getMail(),
                    "Expiration of Booking(s)",
                    "Here your list of expired booking" + stringBuilder
            );
        }
    }
}
