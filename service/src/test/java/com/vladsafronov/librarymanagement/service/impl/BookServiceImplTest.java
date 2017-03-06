package com.vladsafronov.librarymanagement.service.impl;

import com.vladsafronov.librarymanagement.dao.jdbc.DaoErrorMessages;
import com.vladsafronov.librarymanagement.model.Author;
import com.vladsafronov.librarymanagement.model.Book;
import com.vladsafronov.librarymanagement.service.api.BookService;
import com.vladsafronov.librarymanagement.service.impl.integration.ServiceErrorMessages;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test BookService integrating with BookDao
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-service-spring-config.xml"})
@Transactional
public class BookServiceImplTest {

    @Autowired
    BookService bookService;

    public static Author firstAuthor;
    public static Author secondAuthor;
    public static Author thirdAuthor;

    public static  Book firstBook;
    public static  Book secondBook;
    public static  Book thirdBook;
    public static  Book fourthBook;

    public static final int COUNT_OF_BOOKS = 4;

    @Before
    public void setUp(){
        firstAuthor = new Author(1,"George","Orwell", LocalDate.parse("2010-01-01"));
        secondAuthor = new Author(2,"Aldous","Haxley",LocalDate.parse("2009-01-01"));
        thirdAuthor = new Author(3,"Vlad","Safronov",LocalDate.parse("1998-08-22"));

        firstBook = new Book(1,"1984",89,LocalDate.parse("2014-01-01"),"Eng");
        secondBook = new Book(2,"Brave New World",85,LocalDate.parse("2016-01-02"),"Eng");
        thirdBook = new Book (3,"The Road to Wigan Pier",50,LocalDate.parse("2015-01-01"),"Eng");
        fourthBook = new Book(4,"The Doors of Perception",54,LocalDate.parse("2015-02-02"),"Eng");

    }


    @Rule
    public ExpectedException thrown = ExpectedException.none();



    @Test
    public void getAllBooks() throws Exception {
        List<Book> books = bookService.getAllBooks();
        assertNotNull(books);
        assertEquals(COUNT_OF_BOOKS,books.size());
        assertEquals(firstBook,books.get(0));
        assertEquals(secondBook,books.get(1));
    }



    @Test
    public void getBookById() throws Exception {
        Integer id = 1;
        Book actualFirstBook = bookService.getBookById(id);
        id = 2;
        Book actualSecondBook = bookService.getBookById(id);
        assertNotNull(actualFirstBook);
        assertNotNull(actualSecondBook);
        assertEquals(firstBook,actualFirstBook);
        assertEquals(secondBook,actualSecondBook);
    }


    public void getBookByUnexistId(){
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(ServiceErrorMessages.ID_IS_NOT_IN_ACCEPTABLE_RANGE);
        Book book = bookService.getBookById(COUNT_OF_BOOKS+1);
    }

    @Test
    public void getBookByTitle() throws Exception {
        Book actualFirstBook = bookService.getBookByTitle(firstBook.getTitle());
        Book actualSecondBook = bookService.getBookByTitle(secondBook.getTitle());

        assertNotNull(actualFirstBook);
        assertNotNull(actualSecondBook);
        assertEquals(firstBook,actualFirstBook);
        assertEquals(secondBook,actualSecondBook);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getBookByUnexistTitleTest(){
        bookService.getBookByTitle("bla-bla-bla");
    }


    @Test
    public void deleteBookByIdTest(){
        bookService.deleteBookById(firstBook.getId());
        List<Book> books = bookService.getAllBooks();
        Assert.assertEquals(COUNT_OF_BOOKS-1,books.size());

        thrown.expect(EmptyResultDataAccessException.class);
        bookService.getBookById(firstBook.getId());

    }
    @Test
    public void deleteBookByUnexistIdTest(){
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(DaoErrorMessages.ELEMENT_WITH_SUCH_ID_ISNT_EXIST);
        bookService.deleteBookById(COUNT_OF_BOOKS+1);
    }

    @Test
    public void updateBookTest(){
        Book testBook = new Book(firstBook.getId(),"Bla bla bla book",99,LocalDate.now(),"Ru");
        int changedRows = bookService.updateBook(testBook);
        assertEquals(1,changedRows);

        Book actualBook = bookService.getBookById(firstBook.getId());

        assertEquals(testBook,actualBook);
    }

    @Test
    public void updateUnexistBookTest(){
        Book testBook = new Book
                (COUNT_OF_BOOKS+1, "Bla bla bla book",99, LocalDate.now(),"Ru");
        int changedRows = bookService.updateBook(testBook);
        assertEquals(0,changedRows);
    }
    @Test
    public void addBookTest(){
        Book testBook = new Book(null,"New book",99,LocalDate.now(),"Ru");
        bookService.addBook(testBook);
        testBook.setId(COUNT_OF_BOOKS+1);
        List<Book> books = bookService.getAllBooks();
        assertEquals(COUNT_OF_BOOKS+1,books.size());
        Book actualBook = bookService.getBookByTitle(testBook.getTitle());
        assertNotNull(actualBook);
        assertEquals(testBook,actualBook);


        firstBook.setId(null);
        //Try to add duplicate book(because id counter increments even when you try to add book)
        thrown.expect(DuplicateKeyException.class);
        bookService.addBook(firstBook);

        firstBook.setId(1);
    }
    @Test
    public void getBooksFromPeriodTest(){

        List<Book> books = bookService.getBooksFromPeriod(firstBook.getReleaseDate(),secondBook.getReleaseDate());
        assertEquals(COUNT_OF_BOOKS,books.size());
        books = bookService.getBooksFromPeriod(firstBook.getReleaseDate(),LocalDate.now());
        assertEquals(COUNT_OF_BOOKS,books.size());

        books = bookService.getBooksFromPeriod(firstBook.getReleaseDate(),secondBook.getReleaseDate().minusDays(1));
        assertEquals(COUNT_OF_BOOKS-1,books.size());
        for (Book book:books) {
            Assert.assertNotEquals(secondBook,book);
        }

        books = bookService.getBooksFromPeriod(LocalDate.parse("2015-01-01"),secondBook.getReleaseDate());
        assertEquals(COUNT_OF_BOOKS-1,books.size());
        for (Book book:books) {
            Assert.assertNotEquals(firstBook,book);
        }
    }
    @Test
    public void getBooksByAuthorTest(){
        List<Book> books = bookService.getBooksByAuthorId(firstAuthor.getId());
        assertNotNull(books);
        assertEquals(2,books.size());
        assertNotEquals(-1, books.indexOf(firstBook));
        assertNotEquals(-1,books.indexOf(thirdBook));

        books = bookService.getBooksByAuthorId(secondAuthor.getId());
        assertNotNull(books);
        assertEquals(2,books.size());
        assertNotEquals(-1, books.indexOf(secondBook));
        assertNotEquals(-1,books.indexOf(fourthBook));
    }
    @Test
    public void getNoBooksByAuthor(){
        List<Book> books = bookService.getBooksByAuthorId(thirdAuthor.getId());
        assertEquals(0,books.size());
    }


}
