package com.example.graphqlrestapi.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private String id;

    @Column
    private String name;

    @Column
    private Integer pages;

    @JoinColumn(name = "author_id")
    @ManyToOne
    private Author author;

    public Book() {

    }

    public Book(String name, Integer pages, String authorId) {
        this.name = name;
        this.pages = pages;
        this.author = new Author(authorId);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPages() {
        return pages;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

}
