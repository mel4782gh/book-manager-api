package com.techreturners.bookmanager.repository;

import com.techreturners.bookmanager.model.Book;
import com.techreturners.bookmanager.model.Genre;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest //let spring know whole application not required to boot up (may have 100s tests)
public class BookManagerRepositoryTests {

    @Autowired
    private BookManagerRepository bookManagerRepository;

    @Test
    public void testFindAllBooksReturnsBooks() {
        //create new test book
        Book book = new Book(1L, "Book One", "This is the description for Book One", "Person One", Genre.Education);

        //save in repo
        bookManagerRepository.save(book);

        Iterable<Book> books = bookManagerRepository.findAll();
        //assert size is one
        assertThat(books).hasSize(1);

    }

    @Test
    public void testCreatesAndFindBookByIdReturnsBook() {

        Book book = new Book(2L, "Book Two", "This is the description for Book Two", "Person Two", Genre.Fantasy);
        bookManagerRepository.save(book);

        var bookById = bookManagerRepository.findById(book.getId());
        assertThat(bookById).isNotNull();

    }



}
