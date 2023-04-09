package com.infoshare.bookcase.repository;

import com.infoshare.bookcase.dictionary.Category;
import com.infoshare.bookcase.model.Book;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookRepository {

    Book save(Book book);
    Book update(Book book);
    Book findById(UUID id);
    List<Book> findByTitle(String title);
    List<Book> findByCategory(Category category);
    List<Book> findForKids(Boolean forKids);
    List<Book> findAll();
    void deleteById(UUID id);
}
