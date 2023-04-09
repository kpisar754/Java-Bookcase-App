package com.infoshare.bookcase.repository;

import com.infoshare.bookcase.dictionary.Epoch;
import com.infoshare.bookcase.model.Author;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository {

    Author update(Author author);
    Author findById(Long id);
    List<Author> findByLastName(String lastName);
    List<Author> findByCountry(String countryOfOrigin);
    List<Author> findByEpoch(Epoch literaryEpoch);
    List<Author> findAll();
    Long getNextId();
}
