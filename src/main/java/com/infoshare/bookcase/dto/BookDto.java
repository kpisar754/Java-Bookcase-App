package com.infoshare.bookcase.dto;

import com.infoshare.bookcase.dictionary.Category;
import com.infoshare.bookcase.model.Author;
import com.infoshare.bookcase.model.Book;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class BookDto {

    private UUID id;
    private String title;
    private Category category;
    private Integer releaseYear;
    private String editionNumber;
    private Boolean forKids;
    private LocalDate dateAddedToResources;
    private Author author;

    public static BookDto fromBook(Book book) {
        if (Objects.isNull(book)) {
            return null;
        }

        return new BookDto(book.getId(), book.getTitle(), book.getCategory(), book.getReleaseYear(), book.getEditionNumber(), book.getForKids(),
        book.getDateAddedToResources(), book.getAuthor());
    }

    public static List<BookDto> fromBooksList(List<Book> books) {
        return Objects.requireNonNull(books).stream()
                .map(book -> new BookDto(book.getId(), book.getTitle(), book.getCategory(), book.getReleaseYear(), book.getEditionNumber(),
                        book.getForKids(), book.getDateAddedToResources(), book.getAuthor()))
                .toList();
    }
}

