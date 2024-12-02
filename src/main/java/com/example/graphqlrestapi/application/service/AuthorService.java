package com.example.graphqlrestapi.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.graphqlrestapi.application.dto.CreateAuthorRequest;
import com.example.graphqlrestapi.domain.model.Author;
import com.example.graphqlrestapi.domain.repository.AuthorRepository;

@Service
public class AuthorService {
    
    @Autowired
    private AuthorRepository authorRepository;

    public Author createAuthor(CreateAuthorRequest createAuthorRequest) {
        Author author = new Author(
            createAuthorRequest.firstName(),
            createAuthorRequest.lastName()
        );

        authorRepository.saveAndFlush(author);

        return author;
    }

    public Author authorById(String id) {
        return authorRepository.findById(id).orElse(null);
    }

    public List<Author> allAuthors() {
        return authorRepository.findAll();
    }

}
