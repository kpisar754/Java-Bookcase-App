package com.infoshare.bookcase.model;

import com.infoshare.bookcase.dictionary.Century;
import com.infoshare.bookcase.dictionary.Epoch;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "author")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "country", nullable = false)
    private String countryOfOrigin;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "literary_epoch", nullable = false)
    private Epoch literaryEpoch;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "century", nullable = false)
    private Century centuryHeOrSheCreated;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Book> books = new ArrayList<>();

    public Author(String firstName, String lastName, String countryOfOrigin, Epoch literaryEpoch, Century centuryHeOrSheCreated) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.countryOfOrigin = countryOfOrigin;
        this.literaryEpoch = literaryEpoch;
        this.centuryHeOrSheCreated = centuryHeOrSheCreated;
    }

    public Author(String firstName, String lastName, String countryOfOrigin, Epoch literaryEpoch, Century centuryHeOrSheCreated, List<Book> books) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.countryOfOrigin = countryOfOrigin;
        this.literaryEpoch = literaryEpoch;
        this.centuryHeOrSheCreated = centuryHeOrSheCreated;
        this.addBooks(books);
    }

    public void addBooks(List<Book> books) {
        books.forEach(this::addBook);
    }

    public void addBook(Book book) {
        book.setAuthor(this);
        this.books.add(book);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id) && Objects.equals(firstName, author.firstName) && Objects.equals(lastName, author.lastName) && Objects.equals(countryOfOrigin, author.countryOfOrigin) && literaryEpoch == author.literaryEpoch && centuryHeOrSheCreated == author.centuryHeOrSheCreated && Objects.equals(books, author.books);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, countryOfOrigin, literaryEpoch, centuryHeOrSheCreated, books);
    }
}
