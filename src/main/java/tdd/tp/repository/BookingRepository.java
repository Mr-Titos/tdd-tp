package tdd.tp.repository;

import tdd.tp.entity.Booking;
import tdd.tp.enums.BookingStatusEnum;

import java.util.List;


public interface BookingRepository /*extends JpaRepository<Booking, String>*/ {
    Booking findById(String id);

    List<Booking> findAllByStatus(BookingStatusEnum status);

    List<Booking> findAllBySubscriber(String id);

}