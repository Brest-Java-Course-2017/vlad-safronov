package com.vladsafronov.librarymanagement.service.impl;

import com.vladsafronov.librarymanagement.dao.api.BookDao;
import com.vladsafronov.librarymanagement.model.Author;
import com.vladsafronov.librarymanagement.model.Book;
import com.vladsafronov.librarymanagement.service.api.BookService;
import com.vladsafronov.librarymanagement.service.impl.integration.BookServiceErrors;
import org.junit.*;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


import static org.easymock.EasyMock.*;
/**
 * BookServiceImpl mock test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-mock-service-spring-config.xml"})
public class BookServiceMockImplTest {

    @Autowired
    BookService bookService;

    @Autowired
    BookDao mockBookDao;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        reset(mockBookDao);
    }

    @After
    public void tearDown() throws Exception {
        verify(mockBookDao);
    }

    @Test
    public void getAllBooks() {
        List<Book> books = new ArrayList<Book>();
        expect(mockBookDao.getAllBooks()).andReturn(books);
        replay(mockBookDao);
        List<Book> booksFromService = bookService.getAllBooks();
        Assert.assertEquals(books, booksFromService);

    }

    @Test(expected = IllegalArgumentException.class)
    public void getBooksByNullAuthor() {
        replay(mockBookDao);
        Author author = null;
        bookService.getBooksByAuthor(author);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBooksByAuthorWithNullId() {
        replay(mockBookDao);
        Author author = new Author(null, "bla", "bla", LocalDate.now());
        bookService.getBooksByAuthor(author);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBooksByAuthorWithNullName() {
        replay(mockBookDao);
        Author author = new Author(1, null, "bla", LocalDate.now());
        bookService.getBooksByAuthor(author);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBooksByAuthorWithNullSurname() {
        replay(mockBookDao);
        Author author = new Author(1, "bla", null, LocalDate.now());
        bookService.getBooksByAuthor(author);
    }

    @Test
    public void getBooksByAuthor() {
        List<Book> books = new ArrayList<>();
        Author author = new Author(1, "bla", "bla", LocalDate.now());
        expect(mockBookDao.getBooksByAuthor(author)).andReturn(books);
        replay(mockBookDao);

        List<Book> booksFromService = bookService.getBooksByAuthor(author);
        Assert.assertEquals(books, booksFromService);
    }

    @Test
    public void getBookById() {
        Integer id = 1;
        Book book = new Book();
        expect(mockBookDao.getCountOfAllBooks()).andReturn(id + 1);
        expect(mockBookDao.getBookById(id)).andReturn(book);
        replay(mockBookDao);

        Book bookFromService = bookService.getBookById(id);
        assertEquals(book, bookFromService);
    }

    @Test
    public void getBookByIdWhichMoreThenCountOfAllBooks() {
        Integer id = 2;
        expect(mockBookDao.getCountOfAllBooks()).andReturn(id - 1);
        replay(mockBookDao);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(BookServiceErrors.ID_IS_NOT_IN_ACCEPTABLE_RANGE);

        bookService.getBookById(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBookByNullId() {
        replay(mockBookDao);
        Integer id = null;

        bookService.getBookById(id);
    }

    @Test
    public void getBooksFromPeriod() {
        LocalDate firstDate = LocalDate.now().minusDays(1);
        LocalDate secondDate = LocalDate.now().plus(1, ChronoUnit.DAYS);
        List<Book> books = new ArrayList<>();
        expect(mockBookDao.getBooksFromPeriod(firstDate, secondDate)).andReturn(books);
        replay(mockBookDao);

        List<Book> booksFromService = bookService.getBooksFromPeriod(firstDate, secondDate);
        assertEquals(books, booksFromService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBooksFromPeriodWithFutureFromDate() {
        replay(mockBookDao);

        LocalDate firstDate = LocalDate.now().plusDays(1);
        LocalDate secondDate = firstDate.plusDays(1);

        bookService.getBooksFromPeriod(firstDate, secondDate);
    }

    @Test
    public void getBooksFromPeriodWithNullFromDate() {
        LocalDate firstDate = null;
        LocalDate secondDate = LocalDate.now();
        List<Book> books = new ArrayList<>();
        expect(mockBookDao.getBooksFromPeriod(LocalDate.parse("0000-01-01"), secondDate)).andReturn(books);
        replay(mockBookDao);

        List<Book> booksFromService = bookService.getBooksFromPeriod(firstDate, secondDate);
        assertEquals(books, booksFromService);
    }

    @Test
    public void getBooksFromPeriodWithNullToDate() {
        LocalDate firstDate = LocalDate.now().minus(1, ChronoUnit.DAYS);
        LocalDate secondDate = null;
        List<Book> books = new ArrayList<>();
        expect(mockBookDao.getBooksFromPeriod(firstDate, LocalDate.now())).andReturn(books);
        replay(mockBookDao);

        List<Book> booksFromService = bookService.getBooksFromPeriod(firstDate, secondDate);
        assertEquals(books, booksFromService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBooksFromPeriodWhereFromMoreThenTo() {
        replay(mockBookDao);

        LocalDate firstDate = LocalDate.now();
        LocalDate secondDate = LocalDate.now().plus(1, ChronoUnit.DAYS);

        bookService.getBooksFromPeriod(secondDate, firstDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBooksFromPeriodWhereDatesAreEquals() {
        replay(mockBookDao);
        LocalDate firstDate = LocalDate.now();
        LocalDate secondDate = LocalDate.now();
        assertEquals(firstDate, secondDate);

        bookService.getBooksFromPeriod(firstDate, secondDate);
    }

    @Test
    public void getBookByTitle() {
        String someTitle = "someTitle";
        Book someBook = new Book();
        expect(mockBookDao.getBookByTitle(someTitle)).andReturn(someBook);
        replay(mockBookDao);

        Book bookFromService = bookService.getBookByTitle(someTitle);
        assertEquals(someBook, bookFromService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBookByNullTitle() {
        replay(mockBookDao);
        String nullTitle = null;
        bookService.getBookByTitle(nullTitle);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getBookByEmptyTitle() {
        replay(mockBookDao);
        String emptyTitle = "";
        bookService.getBookByTitle(emptyTitle);
    }

    @Test
    public void updateBook() {
        Book book = new Book(1, "bla", 100, LocalDate.now(), "Eng");
        expect(mockBookDao.updateBook(book)).andReturn(1);
        replay(mockBookDao);

        int countOfUpdated = bookService.updateBook(book);
        assertEquals(1, countOfUpdated);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateBookWithNullId() {
        Book book = new Book(null, "bla", 100, LocalDate.now(), "Eng");
        replay(mockBookDao);

        bookService.updateBook(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateBookWithNullTitle() {
        Book book = new Book(1, null, 100, LocalDate.now(), "Eng");
        replay(mockBookDao);

        bookService.updateBook(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateBookWithNullRating() {
        Book book = new Book(1, "title", null, LocalDate.now(), "Eng");
        replay(mockBookDao);

        bookService.updateBook(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateBookWithNullReleaseDate() {
        Book book = new Book(1, "title", 100, null, "Eng");
        replay(mockBookDao);

        bookService.updateBook(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateBookWithNullLang() {
        Book book = new Book(1, "title", 100, LocalDate.now(), null);
        replay(mockBookDao);

        bookService.updateBook(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateBookWithNullArg() {
        Book book = null;
        replay(mockBookDao);

        bookService.updateBook(book);
    }

    @Test
    public void deleteBookById() {
        Integer id = 1;
        mockBookDao.deleteBookById(id);
        expectLastCall();
        replay(mockBookDao);

        bookService.deleteBookById(id);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteBookWithNullId() {
        Integer id = null;
        replay(mockBookDao);

        bookService.deleteBookById(id);
    }

    @Test
    public void deleteBookWithNegativeId() {
        Integer id = -1;
        replay(mockBookDao);

        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(BookServiceErrors.ID_IS_NOT_IN_ACCEPTABLE_RANGE);
        bookService.deleteBookById(id);
    }

    @Test
    public void addBook() {
        Book book = new Book(null, "title", 0, LocalDate.now(), "Eng");
        mockBookDao.addBook(book);
        expectLastCall();
        replay(mockBookDao);

        bookService.addBook(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addBookWithNotNullId() {
        Book book = new Book(1, "title", 0, LocalDate.now(), "Eng");
        replay(mockBookDao);

        bookService.addBook(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addBookWithNullTitle() {
        Book book = new Book(null, null, 0, LocalDate.now(), "Eng");
        replay(mockBookDao);

        bookService.addBook(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addBookWithNullRating() {
        Book book = new Book(null, "title", null, LocalDate.now(), "Eng");
        replay(mockBookDao);

        bookService.addBook(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addBookWithNullReleaseDate() {
        Book book = new Book(null, "title", 0, null, "Eng");
        replay(mockBookDao);

        bookService.addBook(book);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addBookWithNullLang() {
        Book book = new Book(1, "title", 0, LocalDate.now(), null);
        replay(mockBookDao);

        bookService.addBook(book);
    }
    @Test(expected = IllegalArgumentException.class)
    public void addBookWithNullArg(){
        Book book = null;
        replay(mockBookDao);

        bookService.addBook(book);
    }
}