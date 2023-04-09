package com.infoshare.bookcase.dao;

import com.infoshare.bookcase.dictionary.Epoch;
import com.infoshare.bookcase.model.Author;
import com.infoshare.bookcase.repository.AuthorRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Repository
public class AuthorDao implements AuthorRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Author update(Author author) {
        entityManager.merge(author);
        return author;
    }

    @Override
    public Author findById(Long id) {
        return entityManager.find(Author.class, id);
    }

    @Override
    public List<Author> findByLastName(String lastName) {
        if(lastName.length() < 1) {
            return Collections.emptyList();
        }
        String patternForLastName = lastName.trim().toLowerCase() + "%";
        return entityManager.createQuery("SELECT DISTINCT a FROM Author a WHERE lower(a.lastName) LIKE :pattern ORDER BY a.id", Author.class)
                .setParameter("pattern", patternForLastName)
                .getResultList();
    }

    @Override
    public List<Author> findByCountry(String countryOfOrigin) {
        if(countryOfOrigin.length() < 1) {
            return Collections.emptyList();
        }
        String patternForCountryOfOrigin = countryOfOrigin.trim().toLowerCase() + "%";
        return entityManager.createQuery("SELECT DISTINCT a FROM Author a WHERE lower(a.countryOfOrigin) LIKE :pattern ORDER BY a.lastName", Author.class)
                .setParameter("pattern", patternForCountryOfOrigin)
                .getResultList();
    }

    @Override
    public List<Author> findByEpoch(Epoch literaryEpoch) {
        return entityManager.createQuery("SELECT DISTINCT a FROM Author a WHERE a.literaryEpoch = :literary_epoch ORDER BY a.lastName", Author.class)
                .setParameter("literary_epoch", literaryEpoch)
                .getResultList();
    }

    @Override
    public List<Author> findAll() {
        return entityManager.createQuery("SELECT DISTINCT a FROM Author a ORDER BY a.id", Author.class)
                .getResultList();
    }

    @Override
    public Long getNextId() {
        List<Author> authors = findAll();

        return Objects.requireNonNull(authors,"authors cannot be null")
                .stream()
                .mapToLong(Author::getId)
                .reduce(0L, Long::max)
                + 1L;
    }
}
