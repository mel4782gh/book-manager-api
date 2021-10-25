package com.techreturners.bookmanager.service;

import com.techreturners.bookmanager.model.Book;
import com.techreturners.bookmanager.model.Genre;

import com.techreturners.bookmanager.repository.BookManagerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DataJpaTest
public class BookManagerServiceTests {

    @Mock //using test doubles lets you do dependency injection, do not introduce real dependency
    //we need to isolate the bookmanagerservice so test that only
    private BookManagerRepository mockBookManagerRepository;
    //need to give it the test double
    @InjectMocks
    private BookManagerServiceImpl bookManagerServiceImpl;

    @Test
    public void testGetAllBooksReturnsListOfBooks() {

        //Arrange
        //set up some test data as a list
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "Book One", "This is the description for Book One", "Person One", Genre.Education));
        books.add(new Book(2L, "Book Two", "This is the description for Book Two", "Person Two", Genre.Education));
        books.add(new Book(3L, "Book Three", "This is the description for Book Three", "Person Three", Genre.Education));

        //this is a stub - stubbing the return from calling findAll() return books on the mockBookManagerRepository
        //when - is part of Mockqito
        when(mockBookManagerRepository.findAll()).thenReturn(books);

        //may have to wait for this to run and use await
        List<Book> actualResult = bookManagerServiceImpl.getAllBooks();
        //books have been added - check the size
        assertThat(actualResult).hasSize(3);
        //check the actual result vs. expected result
        assertThat(actualResult).isEqualTo(books);
    }

    @Test
    public void testAddABook() {

        var book = new Book(4L, "Book Four", "This is the description for Book Four", "Person Four", Genre.Fantasy);

        when(mockBookManagerRepository.save(book)).thenReturn(book);

        Book actualResult = bookManagerServiceImpl.insertBook(book);

        assertThat(actualResult).isEqualTo(book);
    }

    @Test
    public void testGetBookById() {

        Long bookId = 5L;
        var book = new Book(5L, "Book Five", "This is the description for Book Five", "Person Five", Genre.Fantasy);

        when(mockBookManagerRepository.findById(bookId)).thenReturn(Optional.of(book));

        Book actualResult = bookManagerServiceImpl.getBookById(bookId);

        assertThat(actualResult).isEqualTo(book);
    }

    //User Story 4 - Update Book By Id Solution
    @Test
    public void testUpdateBookById() {

        Long bookId = 5L;
        var book = new Book(5L, "Book Five", "This is the description for Book Five", "Person Five", Genre.Fantasy);

        when(mockBookManagerRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(mockBookManagerRepository.save(book)).thenReturn(book);

        bookManagerServiceImpl.updateBookById(bookId, book);

        verify(mockBookManagerRepository, times(1)).save(book);
    }

    //User Story 5 - Delete Book By Id Solution
    @Test
    public void testDeleteBookById() {
        //Arrange
        //Create a new bookId and book object
        Long bookId = 5L;
        var book = new Book(5L, "Book Five", "This is the description for Book Five", "Person Five", Genre.Fantasy);
        //when findById is called on the mockBookManagerRepository then return book as we are testing the service without dependency on the database
        when(mockBookManagerRepository.findById(bookId)).thenReturn(Optional.of(book));

        //act call the deleteBookById method in the service
        bookManagerServiceImpl.deleteBookById(bookId, book);
        //assert - check that mockBookManagerRepository called delete book once
        verify(mockBookManagerRepository, times(1)).deleteById(bookId);
    }

}
