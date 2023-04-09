package com.infoshare.bookcase.dao;

import com.infoshare.bookcase.dictionary.Category;
import com.infoshare.bookcase.model.Book;
import com.infoshare.bookcase.repository.BookRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Repository
public class BookDao implements BookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Book save(Book book) {
        entityManager.persist(book);
        return book;
    }

    @Override
    public Book update(Book book) {
        entityManager.merge(book);
        return book;
    }

    @Override
    public Book findById(UUID id) {
        return entityManager.find(Book.class, id);
    }

    @Override
    public List<Book> findByTitle(String title) {
        if(title.length() < 3) {
            return Collections.emptyList();
        }
        String patternForTitle = "%" + title.trim().toLowerCase() + "%";
        return entityManager.createQuery("SELECT b FROM Book b JOIN b.author a WHERE lower(b.title) LIKE :pattern ORDER BY b.id", Book.class)
                .setParameter("pattern", patternForTitle)
                .getResultList();
    }

    @Override
    public List<Book> findByCategory(Category category) {
        return entityManager.createQuery("SELECT b FROM Book b JOIN b.author a WHERE b.category = :category ORDER BY a.lastName", Book.class)
                .setParameter("category", category)
                .getResultList();
    }

    @Override
    public List<Book> findForKids(Boolean forKids) {
        return entityManager.createQuery("SELECT b FROM Book b JOIN b.author a WHERE b.forKids = :true ORDER BY a.lastName", Book.class)
                .setParameter("true", forKids)
                .getResultList();
    }

    @Override
    public List<Book> findAll() {
        return entityManager.createQuery("SELECT b FROM Book b JOIN FETCH b.author a ORDER BY a.id", Book.class)
                .getResultList();
    }

    @Override
    public void deleteById(UUID id) {
        Book book = this.findById(id);
        if (book != null) {
            entityManager.remove(book);
        }
    }
}
