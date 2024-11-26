package com.example.graphqlrestapi.application.dto;

public record CreateBookRequest(String name, Integer pages, String authorId) {
    
}