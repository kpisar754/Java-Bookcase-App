package com.infoshare.bookcase.model;

import com.infoshare.bookcase.dictionary.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "book")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Book {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)", name = "id")
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "release_year", nullable = false)
    private Integer releaseYear;

    @Column(name = "edition", nullable = false)
    private String editionNumber;

    @Column(name = "for_kids", nullable = false)
    private Boolean forKids;

    @PastOrPresent
    @Column(name = "date_added", nullable = false)
    private LocalDate dateAddedToResources;

    @ManyToOne
    private Author author;

    public Book(String title, Category category, Integer releaseYear, String editionNumber, Boolean forKids, LocalDate dateAddedToResources, Author author) {
        this.title = title;
        this.category = category;
        this.releaseYear = releaseYear;
        this.editionNumber = editionNumber;
        this.forKids = forKids;
        this.dateAddedToResources = dateAddedToResources;
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(title, book.title) && category == book.category && Objects.equals(releaseYear, book.releaseYear) && Objects.equals(editionNumber, book.editionNumber) && Objects.equals(forKids, book.forKids) && Objects.equals(dateAddedToResources, book.dateAddedToResources) && Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, category, releaseYear, editionNumber, forKids, dateAddedToResources, author);
    }
}
