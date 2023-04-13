package com.infoshare.bookcase.service;

import com.infoshare.bookcase.dictionary.Category;
import com.infoshare.bookcase.dictionary.Epoch;
import com.infoshare.bookcase.dto.AuthorAndBookFormDto;
import com.infoshare.bookcase.dto.AuthorDto;
import com.infoshare.bookcase.dto.BookDto;
import com.infoshare.bookcase.model.Author;
import com.infoshare.bookcase.model.Book;
import com.infoshare.bookcase.repository.AuthorRepository;
import com.infoshare.bookcase.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class BookcaseService {

    private AuthorRepository authorRepository;
    private BookRepository bookRepository;
    private Random generator;


    @Transactional
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Transactional
    public Author updateResource(Author author) {
        return authorRepository.update(author);
    }

    @Transactional
    public Book updateBook(Book book) {
        return bookRepository.update(book);
    }

    @Transactional
    public void deleteBookById(UUID id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public Author findAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    public Book findBookById(UUID id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public List<AuthorDto> findResourcesByAuthorLastName(String lastName) {
        return AuthorDto.fromAuthorsList(authorRepository.findByLastName(lastName));
    }

    @Transactional
    public List<AuthorDto> findResourcesByCountry(String countryOfOrigin) {
        return AuthorDto.fromAuthorsList(authorRepository.findByCountry(countryOfOrigin));
    }

    @Transactional
    public List<AuthorDto> findResourcesByEpoch(Epoch literaryEpoch) {
        return AuthorDto.fromAuthorsList(authorRepository.findByEpoch(literaryEpoch));
    }

    @Transactional
    public List<BookDto> findResourcesByBookTitle(String title) {
        return BookDto.fromBooksList(bookRepository.findByTitle(title));
    }

    @Transactional
    public List<BookDto> findResourcesByBookCategory(Category category) {
        return BookDto.fromBooksList(bookRepository.findByCategory(category));
    }

    @Transactional
    public List<BookDto> findResourcesForKids(Boolean forKids) {
        return BookDto.fromBooksList(bookRepository.findForKids(forKids));
    }

    public List<AuthorDto> findAllResources() {
        return AuthorDto.fromAuthorsList(authorRepository.findAll());
    }

    public Long getNextAuthorId() {
        return authorRepository.getNextId();
    }

    public AuthorAndBookFormDto createAuthorAndBookFormDto() {
        AuthorAndBookFormDto formDto = new AuthorAndBookFormDto();
        formDto.setAuthor(new Author());
        formDto.getAuthor().setId(getNextAuthorId());
        formDto.setBook(new Book());
        formDto.getBook().setDateAddedToResources(LocalDate.now());
        return formDto;
    }

    @Transactional
    public void transferAuthorAndBookToDatabase(AuthorAndBookFormDto formDto) {
        Author author = findAuthorById(formDto.getAuthor().getId());
        if(author == null) {
            author = formDto.getAuthor();
            updateResource(author);
        }
        Book book = formDto.getBook();
        book.setAuthor(author);
        saveBook(book);
    }

    public BookDto provideRandomBook() {
        List<Book> allBooks = bookRepository.findAll();
        if (allBooks.isEmpty()) {
            Book emptyBook = new Book();
            emptyBook.setCategory(Category.NOT_CLASSIFIED);
            emptyBook.setAuthor(new Author());
            return BookDto.fromBook(emptyBook);
        } else {
            int index = generator.nextInt(allBooks.size());
            return BookDto.fromBook(allBooks.get(index));
        }
    }

    public Author fromUpdatedAuthor(Long id, AuthorDto updatedAuthor) {
        Author author = findAuthorById(id);
        author.setFirstName(updatedAuthor.getFirstName());
        author.setLastName(updatedAuthor.getLastName());
        author.setCountryOfOrigin(updatedAuthor.getCountryOfOrigin());
        author.setLiteraryEpoch(updatedAuthor.getLiteraryEpoch());
        author.setCenturyHeOrSheCreated(updatedAuthor.getCenturyHeOrSheCreated());
        return author;
    }

    public Book fromUpdatedBook(UUID id, BookDto updatedBook) {
        Book book = findBookById(id);
        book.setTitle(updatedBook.getTitle());
        book.setCategory(updatedBook.getCategory());
        book.setReleaseYear(updatedBook.getReleaseYear());
        book.setEditionNumber(updatedBook.getEditionNumber());
        book.setDateAddedToResources(updatedBook.getDateAddedToResources());
        return book;
    }

    public Page<AuthorDto> providePagination(Pageable pageable, List<AuthorDto> allResources) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<AuthorDto> authorDtos;

        if (allResources.isEmpty() || allResources.size() < startItem) {
            authorDtos = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allResources.size());
            authorDtos = allResources.subList(startItem, toIndex);
        }

        return new PageImpl<>(authorDtos, pageable, allResources.size());
    }

    public List<Integer> calculatePageNumbers(Page<?> page) {
        int totalPages = page.getTotalPages();
        if (totalPages > 0) {
            int maxVisiblePages = 5;
            int startPage = Math.max(1, page.getNumber() - (maxVisiblePages / 2));
            int endPage = Math.min(totalPages, startPage + maxVisiblePages - 1);
            return IntStream.rangeClosed(startPage, endPage)
                    .boxed()
                    .toList();
        } else {
            return Collections.emptyList();
        }
    }
}
