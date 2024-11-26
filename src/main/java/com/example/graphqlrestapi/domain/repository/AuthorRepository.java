package com.example.graphqlrestapi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.graphql.data.GraphQlRepository;

import com.example.graphqlrestapi.domain.model.Author;

@GraphQlRepository(typeName = "Author")
public interface AuthorRepository extends JpaRepository<Author, String> {
    
}
