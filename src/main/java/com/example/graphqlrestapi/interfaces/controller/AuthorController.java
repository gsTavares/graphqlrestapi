package com.example.graphqlrestapi.interfaces.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.graphqlrestapi.application.dto.CreateAuthorRequest;
import com.example.graphqlrestapi.application.service.AuthorService;
import com.example.graphqlrestapi.domain.model.Author;

@Controller
public class AuthorController {
    
    @Autowired
    private AuthorService authorService;

    @QueryMapping
    public Author authorById(@Argument String id) {
        return authorService.authorById(id);
    }

    @MutationMapping
    public Author createAuthor(@Argument CreateAuthorRequest createAuthorRequest) {
        return authorService.createAuthor(createAuthorRequest);
    }

}
