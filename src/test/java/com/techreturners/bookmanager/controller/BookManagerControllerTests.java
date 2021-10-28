package com.techreturners.bookmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techreturners.bookmanager.model.Book;
import com.techreturners.bookmanager.model.Genre;
import com.techreturners.bookmanager.service.BookManagerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@AutoConfigureMockMvc //use a special mockmvc server, gives a slimmed down version of your app
@SpringBootTest //tells test class to boot up application we have built
public class BookManagerControllerTests {

    @Mock //label as test double
    private BookManagerServiceImpl mockBookManagerServiceImpl;

    @InjectMocks //specifies that this will be injected in constructor
    private BookManagerController bookManagerController;

    @Autowired
    // spring - enables objects to be injected at runtime - we want this mockmvccontroller at runtime - slim down your tests so they start up faster, so do not start full server
    private MockMvc mockMvcController;

    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        //builder configure my controller as mockmvc
        //register my controller against mockmvc
        mockMvcController = MockMvcBuilders.standaloneSetup(bookManagerController).build();
        //to come later
        mapper = new ObjectMapper();
    }

    @Test
    public void testGetAllBooksReturnsBooks() throws Exception {
        //controller's job test display functionality and status code correct. So make bookmanagerservice into a test double
        //test data
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "Book One", "This is the description for Book One", "Person One", Genre.Education));
        books.add(new Book(2L, "Book Two", "This is the description for Book Two", "Person Two", Genre.Education));
        books.add(new Book(3L, "Book Three", "This is the description for Book Three", "Person Three", Genre.Education));
        //stub to return test books whenever getAllBooks is called
        //when - when you do this, this is the result
        when(mockBookManagerServiceImpl.getAllBooks()).thenReturn(books);
        //Assert what are we checking for? provide it with the request
        this.mockMvcController.perform(
                        //do a get request - user googling/postman to GET book
                        MockMvcRequestBuilders.get("/api/v1/book/"))
                //Assert - what are we checking for
                //check that I get status code 200
                .andExpect(MockMvcResultMatchers.status().isOk())
                //check our result from GET all books
                //check the id and the title for each book matches our test data
                //0 is first element of json
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Book One"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Book Two"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].title").value("Book Three"));
    }

    @Test
    public void testGetMappingGetBookById() throws Exception {

        Book book = new Book(4L, "Book Four", "This is the description for Book Four", "Person Four", Genre.Fantasy);

        when(mockBookManagerServiceImpl.getBookById(book.getId())).thenReturn(book);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.get("/api/v1/book/" + book.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Book Four"));
    }

    @Test
    public void testPostMappingAddABook() throws Exception {

        Book book = new Book(4L, "Book Four", "This is the description for Book Four", "Person Four", Genre.Fantasy);

        when(mockBookManagerServiceImpl.insertBook(book)).thenReturn(book);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.post("/api/v1/book/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(book)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(mockBookManagerServiceImpl, times(1)).insertBook(book);
    }

    //User Story 4 - Update Book By Id Solution
    @Test
    public void testPutMappingUpdateABook() throws Exception {

        Book book = new Book(4L, "Fabulous Four", "This is the description for the Fabulous Four", "Person Four", Genre.Fantasy);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.put("/api/v1/book/" + book.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(book)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(mockBookManagerServiceImpl, times(1)).updateBookById(book.getId(), book);
    }

    //User Story 5 - Delete Book By Id Solution
    @Test
    public void testDeleteMappingDeleteABook() throws Exception {

        Book book = new Book(4L, "Fabulous Four", "This is the description for the Fabulous Four", "Person Four", Genre.Fantasy);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.delete("/api/v1/book/" + book.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(book)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //Test that bookmanagerservice deleted a book by checking that deleteBookbyId was called.
        // Controller test uses mock object for the service in order to isolate dependencies.
        verify(mockBookManagerServiceImpl, times(1)).deleteBookById(book.getId(), book);
    }

}
