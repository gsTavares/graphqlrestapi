package com.example.graphqlrestapi.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.graphqlrestapi.application.dto.CreateBookRequest;
import com.example.graphqlrestapi.domain.model.Book;
import com.example.graphqlrestapi.domain.repository.BookRepository;

@Service
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorService authorService;

    public Book createBook(CreateBookRequest createBookRequest) {
        Book book = new Book(
            createBookRequest.name(),
            createBookRequest.pages(),
            createBookRequest.authorId()
        );

        bookRepository.saveAndFlush(book);

        book.setAuthor(authorService.authorById(createBookRequest.authorId()));

        return book;
    }

    public Book bookById(String id) {
        return bookRepository.findById(id)
            .orElse(null);
    }

    public List<Book> booksByAuthor(String authorId) {
        return bookRepository.findByAuthorId(authorId);
    }

    public List<Book> allBooks() {
        return bookRepository.findAll();
    }

}
