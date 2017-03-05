package com.vladsafronov.librarymanagement.service.impl;

import com.vladsafronov.librarymanagement.dao.api.AuthorDao;
import com.vladsafronov.librarymanagement.model.Author;
import com.vladsafronov.librarymanagement.model.Book;
import com.vladsafronov.librarymanagement.service.api.AuthorService;
import com.vladsafronov.librarymanagement.service.impl.integration.ServiceErrorMessages;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * AuthorService mock tests
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-mock-service-spring-config.xml"})
public class AuthorServiceMockImplTest {

    @Autowired
    AuthorDao mockAuthorDao;
    @Autowired
    AuthorService authorService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp(){
        reset(mockAuthorDao);
    }
    @After
    public void tearDown(){
        verify(mockAuthorDao);
    }

    @Test
    public void getAllAuthors(){
        List<Author> authors = new ArrayList<>();
        expect(mockAuthorDao.getAllAuthors()).andReturn(authors);
        replay(mockAuthorDao);

        List<Author> authorsFromService = authorService.getAllAuthors();
        assertEquals(authors,authorsFromService);
    }


    @Test
    public void getAuthorById() {
        Integer id = 1;
        Author author = new Author();
        expect(mockAuthorDao.getCountOfAllAuthors()).andReturn(id + 1);
        expect(mockAuthorDao.getAuthorById(id)).andReturn(author);
        replay(mockAuthorDao);

        Author authorFromService = authorService.getAuthorById(id);
        assertEquals(author, authorFromService);
    }


    @Test
    public void getAuthorByIdWhichMoreThenCountOfAllBooks() {
        Integer id = 2;
        expect(mockAuthorDao.getCountOfAllAuthors()).andReturn(id - 1);
        replay(mockAuthorDao);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(ServiceErrorMessages.ID_IS_NOT_IN_ACCEPTABLE_RANGE);

        authorService.getAuthorById(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAuthorByNullId() {
        replay(mockAuthorDao);
        Integer id = null;

        authorService.getAuthorById(id);
    }

    @Test
    public void addAuthor(){
        Author author = new Author(null,"Name","Surname",LocalDate.now());
        mockAuthorDao.addAuthor(author);
        expectLastCall();
        replay(mockAuthorDao);

        authorService.addAuthor(author);
    }
    @Test(expected = IllegalArgumentException.class)
    public void addNullAuthor(){
        Author author = null;
        replay(mockAuthorDao);

        authorService.addAuthor(author);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addAuthorWithNotNullId(){
        Author author = new Author(1,"Name","Surname",LocalDate.now());
        replay(mockAuthorDao);

        authorService.addAuthor(author);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addAuthorWithNullName(){
        Author author = new Author(null,null,"Surname",LocalDate.now());
        replay(mockAuthorDao);

        authorService.addAuthor(author);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addAuthorWithNullSurname(){
        Author author = new Author(null,"name",null,LocalDate.now());
        replay(mockAuthorDao);

        authorService.addAuthor(author);
    }

    @Test
    public void addAuthorWithNullBirthDate(){
        Author author = new Author(null,"name","Surname",AuthorServiceImpl.DEFAULT_BIRTH_DATE);
        mockAuthorDao.addAuthor(author);
        replay(mockAuthorDao);

        author.setBirthDate(null);
        authorService.addAuthor(author);
    }

    @Test
    public void deleteAuthorById() {
        Integer id = 1;
        mockAuthorDao.deleteAuthorById(id);
        expectLastCall();
        replay(mockAuthorDao);

        authorService.deleteAuthorById(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteAuthorWithNullId() {
        Integer id = null;
        replay(mockAuthorDao);

        authorService.deleteAuthorById(id);
    }

    @Test
    public void deleteAuthorWithNegativeId() {
        Integer id = -1;
        replay(mockAuthorDao);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(ServiceErrorMessages.ID_IS_NOT_IN_ACCEPTABLE_RANGE);
        authorService.deleteAuthorById(id);
    }

    @Test
    public void updateAuthor(){
        Author author = new Author(1,"name","surname",LocalDate.now());
        expect(mockAuthorDao.updateAuthor(author)).andReturn(1);
        replay(mockAuthorDao);

        int count = authorService.updateAuthor(author);
        assertEquals(1,count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateAuthorWithNullArg(){
        Author author = null;
        replay(mockAuthorDao);

        authorService.updateAuthor(author);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateAuthorWithNullId(){
        replay(mockAuthorDao);

        Author author = new Author(null,"name","surname",LocalDate.now());
        authorService.updateAuthor(author);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateAuthorWithNullName() {
        replay(mockAuthorDao);

        Author author = new Author(1, null, "surname", LocalDate.now());
        authorService.updateAuthor(author);
    }


    @Test(expected = IllegalArgumentException.class)
    public void updateAuthorWithNullSurname() {
        replay(mockAuthorDao);

        Author author = new Author(1, "name", null, LocalDate.now());
        authorService.updateAuthor(author);
    }


    @Test(expected = IllegalArgumentException.class)
    public void updateAuthorWithNullBirthDate() {
        replay(mockAuthorDao);

        Author author = new Author(1, "name", "surname", null);
        authorService.updateAuthor(author);
    }

    @Test
    public void getCountOfAuthorsBooks(){
        Integer count = 2;
        Author author = new Author(1, "name", "surname", LocalDate.now());
        expect(mockAuthorDao.getCountOfAuthorsBooks(author)).andReturn(count);
        replay(mockAuthorDao);

        Integer countFromService = authorService.getCountOfAuthorsBooks(author);
        assertEquals(count,countFromService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCountOfAuthorsBooksWithNullAuthor(){
        replay(mockAuthorDao);
        Author author = null;
        authorService.getCountOfAuthorsBooks(author);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCountOfAuthorsBooksWithNullAuthorId(){
        replay(mockAuthorDao);
        Author author = new Author(null,"name","surname",LocalDate.now());
        authorService.getCountOfAuthorsBooks(author);
    }

    @Test
    public void getCountOfAuthorsBooksWithNegativeAuthorId(){
        replay(mockAuthorDao);
        Author author = new Author(-1,"name","surname",LocalDate.now());

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(ServiceErrorMessages.ID_IS_NOT_IN_ACCEPTABLE_RANGE);

        authorService.getCountOfAuthorsBooks(author);
    }
    //TODO what about other field?


    @Test
    public void getAverageRatingOfAuthorsBooks(){
        Double averageRating = 50d;
        Author author = new Author(1, "name", "surname", LocalDate.now());
        expect(mockAuthorDao.getAverageRatingOfAuthorsBooks(author)).andReturn(averageRating);
        replay(mockAuthorDao);

        Double countFromService = authorService.getAverageRatingOfAuthorsBooks(author);
        double delta = 0.01;
        assertEquals(averageRating,countFromService,delta);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAverageRatingOfAuthorsBooksWithNullArg(){
        replay(mockAuthorDao);
        Author author = null;

        authorService.getAverageRatingOfAuthorsBooks(author);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAverageRatingOfAuthorsBooksWithNullAuthorId(){
        replay(mockAuthorDao);
        Author author = new Author(null, "name", "surname", LocalDate.now());

        authorService.getAverageRatingOfAuthorsBooks(author);
    }

    @Test
    public void getAverageRatingOfAuthorsBooksWithNegativeAuthorId(){
        replay(mockAuthorDao);

        Author author = new Author(-1, "name", "surname", LocalDate.now());

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(ServiceErrorMessages.ID_IS_NOT_IN_ACCEPTABLE_RANGE);

        authorService.getAverageRatingOfAuthorsBooks(author);
    }

    @Test
    public void getAuthorByBook(){
        Book book = new Book(1,"title",50,LocalDate.now(),"Eng");
        Author author = new Author();
        expect(mockAuthorDao.getAuthorByBook(book)).andReturn(author);
        replay(mockAuthorDao);

        Author authorFromService = authorService.getAuthorByBook(book);
        assertEquals(author,authorFromService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAuthorByBookWithNullBookId(){
        Book book = new Book(null,"title",50,LocalDate.now(),"Eng");
        replay(mockAuthorDao);

        authorService.getAuthorByBook(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAuthorByBookWithNullBookTitle(){
        Book book = new Book(1,null,50,LocalDate.now(),"Eng");
        replay(mockAuthorDao);

        authorService.getAuthorByBook(book);
    }

    @Test
    public void getAuthorByBookWithNegativeBookId(){
        Book book = new Book(-1,"title",50,LocalDate.now(),"Eng");
        replay(mockAuthorDao);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(ServiceErrorMessages.ID_IS_NOT_IN_ACCEPTABLE_RANGE);
        authorService.getAuthorByBook(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAuthorByNullBook(){
        Book book = null;
        replay(mockAuthorDao);

        authorService.getAuthorByBook(book);
    }
}
