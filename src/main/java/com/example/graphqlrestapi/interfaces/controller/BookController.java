package com.example.graphqlrestapi.interfaces.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.graphqlrestapi.application.dto.CreateBookRequest;
import com.example.graphqlrestapi.application.service.BookService;
import com.example.graphqlrestapi.domain.model.Book;

@Controller
public class BookController {
    
    @Autowired
    private BookService bookService;

    @MutationMapping
    public Book createBook(@Argument CreateBookRequest createBookRequest) {
        return bookService.createBook(createBookRequest);
    }

    @QueryMapping
    public Book bookById(@Argument String id) {
        return bookService.bookById(id);
    }

    @QueryMapping
    public List<Book> booksByAuthor(@Argument String authorId) {
        return bookService.booksByAuthor(authorId);
    }

    @QueryMapping
    public List<Book> allBooks() {
        return bookService.allBooks();
    }

}
