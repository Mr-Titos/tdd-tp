package tdd.tp.entity;

import lombok.*;
import tdd.tp.enums.BookSizeEnum;

@Builder
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Book {
    private String isbn;

    private String title;

    private int nbPage;

    private String author;

    private String editor;

    private BookSizeEnum size = null;

    private boolean isAvailable;

    public Book(Book b) {
        this(b.getIsbn(), b.getTitle(), b.getNbPage(), b.getAuthor(), b.getEditor(), b.getSize(), b.isAvailable());
    }
}
