package com.infoshare.bookcase.dto;

import com.infoshare.bookcase.dictionary.Century;
import com.infoshare.bookcase.dictionary.Epoch;
import com.infoshare.bookcase.model.Author;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AuthorDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String countryOfOrigin;
    private Epoch literaryEpoch;
    private Century centuryHeOrSheCreated;
    private List<BookDto> booksDtos;

    public static AuthorDto fromAuthor(Author author) {
        if (Objects.isNull(author)) {
            return null;
        }

        List<BookDto> booksDtos = new ArrayList<>();

        if(author.getBooks() != null) {
            author.getBooks().forEach(book -> booksDtos.add(
                    new BookDto(book.getId(), book.getTitle(), book.getCategory(), book.getReleaseYear(),
                            book.getEditionNumber(), book.getForKids(), book.getDateAddedToResources(), book.getAuthor())));
        }

        return new AuthorDto(author.getId(), author.getFirstName(), author.getLastName(), author.getCountryOfOrigin(),
                author.getLiteraryEpoch(), author.getCenturyHeOrSheCreated(), booksDtos);
    }

    public static List<AuthorDto> fromAuthorsList(List<Author> authors) {
        return Objects.requireNonNull(authors).stream()
                .map(author -> {
                    List<BookDto> booksDtos = author.getBooks().stream()
                            .map(book -> new BookDto(book.getId(), book.getTitle(), book.getCategory(), book.getReleaseYear(),
                                    book.getEditionNumber(), book.getForKids(), book.getDateAddedToResources(), book.getAuthor()))
                            .toList();
                    return new AuthorDto(author.getId(), author.getFirstName(), author.getLastName(), author.getCountryOfOrigin(),
                            author.getLiteraryEpoch(), author.getCenturyHeOrSheCreated(), booksDtos);
                })
                .toList();
    }
}
