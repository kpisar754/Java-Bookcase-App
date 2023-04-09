package com.infoshare.bookcase.controller;

import com.infoshare.bookcase.dictionary.Category;
import com.infoshare.bookcase.dictionary.Epoch;
import com.infoshare.bookcase.dto.AuthorAndBookFormDto;
import com.infoshare.bookcase.dto.AuthorDto;
import com.infoshare.bookcase.dto.BookDto;
import com.infoshare.bookcase.model.Author;
import com.infoshare.bookcase.model.Book;
import com.infoshare.bookcase.service.BookcaseService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;



@AllArgsConstructor
@Controller
public class PageViewController {

    private BookcaseService bookcaseService;

    @GetMapping("/")
    public ModelAndView showStartMenu() {
        ModelAndView mav = new ModelAndView("index");
        mav.addObject("message","Welcome to Bookcase");
        return mav;
    }

    @GetMapping("/admin/api/books/library")
    @Secured("ROLE_ADMIN")
    public ModelAndView showBookcase(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "8") int size) {
        ModelAndView mav = new ModelAndView("bookcase");
        List<AuthorDto> authorDtoList = bookcaseService.findAllResources();
        Page<AuthorDto> authorDtoPage = bookcaseService.providePagination(PageRequest.of(page - 1, size), authorDtoList);
        mav.addObject("authorDtoPage", authorDtoPage);
        mav.addObject("pageNumbers", bookcaseService.calculatePageNumbers(authorDtoPage));
        return mav;
    }

    @GetMapping("/admin/api/books/add")
    @Secured("ROLE_ADMIN")
    public ModelAndView addResource() {
        ModelAndView mav = new ModelAndView("add");
        AuthorAndBookFormDto formDto = bookcaseService.createAuthorAndBookFormDto();
        List<AuthorDto> authorDtoList = bookcaseService.findAllResources();
        mav.addObject("form", formDto);
        mav.addObject("authors", authorDtoList);
        return mav;
    }

    @PostMapping("/api/books/add")
    public ModelAndView addResourceForm(@ModelAttribute("form") AuthorAndBookFormDto formDto ) {
        ModelAndView mav = new ModelAndView("redirect:/");
        bookcaseService.transferAuthorAndBookToDatabase(formDto);
        return mav;
    }

    @GetMapping("/delete-book/{id}")
    public ModelAndView deleteBook(@PathVariable UUID id) {
        ModelAndView mav = new ModelAndView("redirect:/");
        bookcaseService.deleteBookById(id);
        return mav;
    }

    @GetMapping("/update-author/{id}")
    public ModelAndView updateAuthor(@PathVariable Long id) {
        ModelAndView mav = new ModelAndView("authorUpdate");
        Author author = bookcaseService.findAuthorById(id);
        AuthorDto authorDto = AuthorDto.fromAuthor(author);
        mav.addObject("author", authorDto);
        return mav;
    }

    @GetMapping("/author/update/{id}")
    public ModelAndView updateAuthorFrom(@PathVariable Long id, @ModelAttribute("author") AuthorDto updatedAuthorDto) {
        ModelAndView mav = new ModelAndView("redirect:/");
        Author author = bookcaseService.fromUpdatedAuthor(id, updatedAuthorDto);
        bookcaseService.updateResource(author);
        return mav;
    }

    @GetMapping("/update-book/{id}")
    public ModelAndView updateBook(@PathVariable UUID id) {
        ModelAndView mav = new ModelAndView("bookUpdate");
        Book book = bookcaseService.findBookById(id);
        BookDto bookDto = BookDto.fromBook(book);
        mav.addObject("book", bookDto);
        return mav;
    }

    @GetMapping("/book/update/{id}")
    public ModelAndView updateBookForm(@PathVariable UUID id, @ModelAttribute("book") BookDto updatedBookDto) {
        ModelAndView mav = new ModelAndView("redirect:/");
        Book book = bookcaseService.fromUpdatedBook(id, updatedBookDto);
        bookcaseService.updateBook(book);
        return mav;
    }

    @GetMapping("api/book/random")
    public ModelAndView showRandomBook() {
        ModelAndView mav = new ModelAndView("random");
        BookDto book = bookcaseService.provideRandomBook();
        mav.addObject("book", book);
        return mav;
    }

    @GetMapping("/public/api/books/library/lastName")
    public ModelAndView showFoundAuthorsByName(@RequestParam String lastName) {
        ModelAndView mav = new ModelAndView("authors");
        List<AuthorDto> authorDtoList = bookcaseService.findResourcesByAuthorLastName(lastName);
        mav.addObject("authors", authorDtoList);
        return mav;
    }

    @GetMapping("api/books/library/country")
    public ModelAndView showFoundAuthorsByCountry(@RequestParam String country) {
        ModelAndView mav = new ModelAndView("authors");
        List<AuthorDto> authorDtoList = bookcaseService.findResourcesByCountry(country);
        mav.addObject("authors", authorDtoList);
        return mav;
    }

    @GetMapping("api/books/library/literaryEpoch")
    public ModelAndView showFoundAuthorsByEpoch(@RequestParam Epoch literaryEpoch) {
        ModelAndView mav = new ModelAndView("authors");
        List<AuthorDto> authorDtoList = bookcaseService.findResourcesByEpoch(literaryEpoch);
        mav.addObject("authors", authorDtoList);
        return mav;
    }

    @GetMapping("api/book/find")
    public ModelAndView showFoundBooksByTitle(@RequestParam String title) {
        ModelAndView mav = new ModelAndView("find");
        List<BookDto> books = bookcaseService.findResourcesByBookTitle(title);
        mav.addObject("books", books);
        return mav;
    }

    @GetMapping("api/book/category/find")
    public ModelAndView showFoundBooksByCategory(@RequestParam Category category) {
        ModelAndView mav = new ModelAndView("find");
        List<BookDto> books = bookcaseService.findResourcesByBookCategory(category);
        mav.addObject("books", books);
        return mav;
    }

    @GetMapping("api/book/for_kids/find")
    public ModelAndView showFoundBooksForKids(@RequestParam Boolean forKids) {
        ModelAndView mav = new ModelAndView("find");
        List<BookDto> books = bookcaseService.findResourcesForKids(forKids);
        mav.addObject("books", books);
        return mav;
    }

    @GetMapping("/login_form")
    public ModelAndView showLogin() {
        return new ModelAndView("loginForm");
    }

    @GetMapping("/logout_form")
    public ModelAndView showLogout() {
        return new ModelAndView("logoutForm");
    }
}

