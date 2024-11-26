package com.example.graphqlrestapi.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.graphql.data.GraphQlRepository;

import com.example.graphqlrestapi.domain.model.Book;

@GraphQlRepository(typeName = "Book")
public interface BookRepository extends JpaRepository<Book, String> {
 
    List<Book> findByAuthorId(String authorId);
    
}
