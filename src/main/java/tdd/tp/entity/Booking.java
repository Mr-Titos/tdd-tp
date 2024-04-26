package tdd.tp.entity;

import lombok.*;
import tdd.tp.enums.BookingStatusEnum;

import java.time.LocalDate;

@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Booking {
    private String id;

    private Subscriber subscriber;

    private LocalDate entryDate;

    private LocalDate limitDate;

    private BookingStatusEnum status;

    private Book book;
}
