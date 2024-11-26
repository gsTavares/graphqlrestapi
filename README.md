# Aplicando o básico de GraphQL com Spring

## Por que e para que GraphQL?

O GraphQL é uma alternativa de solução para a construção de API's, criado para resolver dois problemas recorrentes encontrados no padrão REST, sendo eles:

- Overfetching
- Underfetching

"Fetch" na tradução literal significa "buscar". Então **"Overfetching"** seria algo como **"busca excessiva"** e **"Underfetching"** significa **"busca insuficiente"**. 

Trazendo isso para o padrão REST, Overfetching é quando temos endpoints que retornam informações além do necessário para outras aplicações ou serviços (ex. Quando devolvemos uma _@Entity_ por completo em alguns retornos \[Não recomendo fazerem isso rsrs\]). Já o Underfetching seria o contrário: quando temos retornos enxutos ou resumidos demais, dificultando o consumo por parte de outras aplicações.

O fato é que isso gera uma manutenção recorrente, se as regras de negócio entre os serviços REST e as outras aplicações não forem bem definidas.

O GraphQL veio para simplificar esse "contrato" por meio de uma maior flexibilidade de consultas, redução da quantidade de protocolos HTTP expostos (no caso, tudo se resume a uma rota POST) e uma filosofia que se resume a dois comportamentos: **Queries** e **Mutations**

**Queries**: são utilizadas para consultar dados

**Mutations**: são utilizadas para a persistência de dados (e podem retornar dados também)

## Contexto do projeto

Aqui temos a representação de duas entidades: **Book** e **Author**, sendo que um autor pode escrever um ou mais livros. E um livro, obrigatoriamente, possui um único author


Entity Author
```java
package com.example.graphqlrestapi.domain.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column
    private String id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @OneToMany(mappedBy = "author")
    private List<Book> books;

    public Author() {

    }

    public Author(String id) {
        this.id = id;
    }

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Book> getBooks() {
        return books;
    }

}
```

Entity Book
```java
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
```

Para o GraphQL, essas entidades são mapeadas em um *schema* como *types*

```graphql
type Book {
    id: ID!,
    name: String!,
    pages: Int!,
    author: Author!
}

type Author {
    id: ID!,
    firstName: String!,
    lastName: String!,
    books: [Book!]!
}
```

Além das entidades, precisamos também definir quais são as nossas **Queries** e **Mutations**

```graphql
type Query {
    bookById(id: ID): Book,
    authorById(id: ID): Author
    booksByAuthor(authorId: String): [Book]
}

type Mutation {
    createAuthor(createAuthorRequest: CreateAuthorRequest): Author,
    createBook(createBookRequest: CreateBookRequest): Book
}
```

As queries e as mutations podem receber parâmetros. A diferença é que, se quisermos utilizar uma estrutura de dados mais complexa como entradas, devemos mapeá-las como **inputs**

```graphql
input CreateAuthorRequest {
    firstName: String!,
    lastName: String!
}

input CreateBookRequest {
    name: String!,
    pages: Int!,
    authorId: ID!
}
```

Por meio da anotação @GrapqhqlRepository, podemos dizer ao Spring qual é o vínculo entre a entidade que mapeamos com o type correspondente no GraphQL

```java
package com.example.graphqlrestapi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.graphql.data.GraphQlRepository;

import com.example.graphqlrestapi.domain.model.Author;

@GraphQlRepository(typeName = "Author")
public interface AuthorRepository extends JpaRepository<Author, String> {
    
}
```

Por fim, após a construção da camada de serviço, podemos mapear nossas queries e mutations por meio de controllers

```java
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
```

## Documentação e testes

A interação com o GraphQL pode ser feita de duas formas

- [GraphiQL](https://github.com/graphql/graphiql/tree/main/packages/graphiql#readme) (pode ser acessado em `http://localhost:8081/graphiql`)
- HTTP (`POST http://localhost:8081/grapqhql`)

O GraphiQL é uma interface onde é possível testar as queries e mutations utilizando a sintaxe do GraphQL. Já a segunda opção geralmente é utilizada por outras aplicações que precisam se comunicar com um servidor GrapqhQL (ex. aplicativos, websites etc.)