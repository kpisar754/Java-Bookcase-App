package com.infoshare.bookcase.dto;

import com.infoshare.bookcase.model.Author;
import com.infoshare.bookcase.model.Book;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class AuthorAndBookFormDto {
    private Author author;
    private Book book;
}
