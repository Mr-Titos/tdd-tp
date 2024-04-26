package tdd.tp.service.booking;

import tdd.tp.entity.Booking;
import tdd.tp.enums.BookingStatusEnum;
import tdd.tp.repository.BookingRepository;
import tdd.tp.service.DataService;

import java.util.List;

public class BookingDataDBService implements DataService<Booking> {
    private static BookingDataDBService session = null;
    private BookingRepository bookingRepository;

    private BookingDataDBService() {}

    public BookingDataDBService getSession() {
        if (session == null)
            session = new BookingDataDBService();
        return session;
    }

    @Override
    public Booking findByID(String id) {
        Booking b = bookingRepository.findById(id);
        throw new RuntimeException("Not Implemented");
    }

    public List<Booking> findAllByStatus(BookingStatusEnum status) {
        List<Booking> bookings = bookingRepository.findAllByStatus(status);
        throw new RuntimeException("Not Implemented");
    }

    public List<Booking> findAllBySubscriber(String id) {
        List<Booking> bookings = bookingRepository.findAllBySubscriber(id);
        throw new RuntimeException("Not Implemented");
    }

    public Booking updateBooking(Booking b) {
        throw new RuntimeException("Not Implemented yet");
    }

    public Booking createBooking(Booking b) {
        throw new RuntimeException("Not implemented yet");
    }

    public void deleteBooking(Booking b) {
        throw new RuntimeException("Not implemented yet");
    }
}
