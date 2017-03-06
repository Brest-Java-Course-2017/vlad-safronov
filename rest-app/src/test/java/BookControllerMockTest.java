import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladsafronov.librarymanagement.model.Author;
import com.vladsafronov.librarymanagement.model.Book;
import com.vladsafronov.librarymanagement.rest.BookController;
import com.vladsafronov.librarymanagement.service.api.BookService;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

/**
 * Test
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-rest-spring-config.xml"})
public class BookControllerMockTest {

    @Resource
    BookController bookController;

    @Autowired
    BookService mockBookService;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = standaloneSetup(bookController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    @Before
    public void resetMock(){
        reset(mockBookService);
    }
    @After
    public void verifyMock() {
        verify(mockBookService);
    }

    public static final Author firstAuthor = new Author(1,"George","Orwell", LocalDate.parse("2010-01-01"));
    public static final Author secondAuthor = new Author(2,"Aldous","Haxley",LocalDate.parse("2009-01-01"));
    public static final Author thirdAuthor = new Author(3,"Vlad","Safronov",LocalDate.parse("1998-08-22"));

    public static final Book firstBook = new Book(1,"1984",89,LocalDate.parse("2014-01-01"),"Eng");
    public static final Book secondBook = new Book(2,"Brave New World",85,LocalDate.parse("2016-01-02"),"Eng");
    public static final Book thirdBook = new Book (3,"The Road to Wigan Pier",50,LocalDate.parse("2015-01-01"),"Eng");
    public static final Book fourthBook = new Book(4,"The Doors of Perception",54,LocalDate.parse("2015-02-02"),"Eng");



    @Test
    public void getAllBooks() throws Exception {
        List<Book> books = new ArrayList<>();
        expect(mockBookService.getAllBooks()).andReturn(books).times(2);
        replay(mockBookService);

        String emptyList = new ObjectMapper().writeValueAsString(books);

        mockMvc.perform(
                get("/books")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(emptyList));

        books.add(firstBook);
        books.add(secondBook);

        String list = new ObjectMapper().writeValueAsString(books);

        mockMvc.perform(
                get("/books")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(list));

    }

    @Test
    public void getBookById() throws Exception {
        Integer id = 2;
        expect(mockBookService.getBookById(id)).andReturn(secondBook);
        replay(mockBookService);

        String book = new ObjectMapper().writeValueAsString(secondBook);
        mockMvc.perform(
                get("/book/"+id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(book));
    }

    @Test
    public void getBookByTitle() throws Exception {
        expect(mockBookService.getBookByTitle(firstBook.getTitle()))
                .andReturn(firstBook);
        replay(mockBookService);

        String book = new ObjectMapper().writeValueAsString(firstBook);


        mockMvc.perform(get("/book").param("title",firstBook.getTitle())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)

        )
                .andExpect(status().isOk())
                .andExpect(content().string(book));
    }

    @Test
    public void deleteBookById() throws Exception{
        mockBookService.deleteBookById(firstBook.getId());
        expectLastCall();
        replay(mockBookService);

        mockMvc.perform(delete("/book/"+firstBook.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getBooksFromPeriod() throws Exception{
        List<Book> books = new ArrayList<>();
        books.add(firstBook);
        books.add(secondBook);

        expect(mockBookService.getBooksFromPeriod
                (firstBook.getReleaseDate(),secondBook.getReleaseDate()))
                .andReturn(books);
        replay(mockBookService);

        MultiValueMap<String,String> map = new LinkedMultiValueMap<>();
        map.add("from",firstBook.getStrReleaseDate());
        map.add("to",secondBook.getStrReleaseDate());

        String resBooks = new ObjectMapper().writeValueAsString(books);

        mockMvc.perform(get("/book/period").params(map)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().string(resBooks));

    }

    @Test
    public void updateBook() throws Exception {
        expect(mockBookService.updateBook(firstBook)).andReturn(1);
        replay(mockBookService);

        String body = new ObjectMapper().writeValueAsString(firstBook);
        String result = "1";

        mockMvc.perform(put("/books")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(result));
    }

    @Test
    public void addBook() throws Exception {
        Book book = new Book
                (null,"title",50,LocalDate.now(),"eng");
        mockBookService.addBook(book);
        expectLastCall();
        replay(mockBookService);

        String body = new ObjectMapper().writeValueAsString(book);

        mockMvc.perform(post("/books")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void getBooksByAuthorId() throws Exception{
        List<Book> books = new ArrayList<>();
        books.add(firstBook);
        books.add(secondBook);
        expect(mockBookService.getBooksByAuthorId(firstAuthor.getId()))
                .andReturn(books);
        replay(mockBookService);

        String expectedResponse = new ObjectMapper().writeValueAsString(books);

        mockMvc.perform(get("/author/"+firstAuthor.getId()+"/books"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

    }

}
