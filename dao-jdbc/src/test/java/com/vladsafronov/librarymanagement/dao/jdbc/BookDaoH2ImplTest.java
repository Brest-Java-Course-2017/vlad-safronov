package com.vladsafronov.librarymanagement.dao.jdbc;

import com.vladsafronov.librarymanagement.dao.api.BookDao;
import com.vladsafronov.librarymanagement.dao.jdbc.BookDaoErrors;
import com.vladsafronov.librarymanagement.model.Author;
import com.vladsafronov.librarymanagement.model.Book;
import org.junit.Assert;
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


import java.util.*;

import static org.junit.Assert.*;
import java.time.LocalDate;

/**
 * Book dao implementation test
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-spring-config.xml"})
@Transactional
public class BookDaoH2ImplTest {

    public static final Author firstAuthor = new Author(1,"George","Orwell",LocalDate.parse("2010-01-01"));
    public static final Author secondAuthor = new Author(2,"Aldous","Haxley",LocalDate.parse("2009-01-01"));
    public static final Author thirdAuthor = new Author(3,"Vlad","Safronov",LocalDate.parse("1998-08-22"));

    public static final Book firstBook = new Book(1,"1984",89,LocalDate.parse("2014-01-01"),"Eng");
    public static final Book secondBook = new Book(2,"Brave New World",85,LocalDate.parse("2016-01-02"),"Eng");
    public static final Book thirdBook = new Book (3,"The Road to Wigan Pier",50,LocalDate.parse("2015-01-01"),"Eng");
    public static final Book fourthBook = new Book(4,"The Doors of Perception",54,LocalDate.parse("2015-02-02"),"Eng");

    public static final int COUNT_OF_BOOKS = 4;

    @Autowired
    BookDao bookDao;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test
    public void getCountOfAllBooks(){
        assertEquals(COUNT_OF_BOOKS,bookDao.getCountOfAllBooks());
    }
    @Test
    public void getCountOfAllBooksIfHasNoBooks(){
        for (int i = 0; i < COUNT_OF_BOOKS; i++) {
            bookDao.deleteBookById(i);
        }
        assertEquals(0,bookDao.getCountOfAllBooks());
    }


    @Test
    public void getAllBooks() throws Exception {
        List<Book> books = bookDao.getAllBooks();
        assertNotNull(books);
        assertEquals(COUNT_OF_BOOKS,books.size());
        assertEquals(firstBook,books.get(0));
        assertEquals(secondBook,books.get(1));
    }



    @Test
    public void getBookById() throws Exception {
        Integer id = 1;
        Book actualFirstBook = bookDao.getBookById(id);
        id = 2;
        Book actualSecondBook = bookDao.getBookById(id);
        assertNotNull(actualFirstBook);
        assertNotNull(actualSecondBook);
        assertEquals(firstBook,actualFirstBook);
        assertEquals(secondBook,actualSecondBook);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getBookByUnexistId(){
        Book book = bookDao.getBookById(COUNT_OF_BOOKS+1);
    }

    @Test
    public void getBookByTitle() throws Exception {
        Book actualFirstBook = bookDao.getBookByTitle(firstBook.getTitle());
        Book actualSecondBook = bookDao.getBookByTitle(secondBook.getTitle());

        assertNotNull(actualFirstBook);
        assertNotNull(actualSecondBook);
        assertEquals(firstBook,actualFirstBook);
        assertEquals(secondBook,actualSecondBook);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getBookByUnexistTitleTest(){
        bookDao.getBookByTitle("bla-bla-bla");
    }

    @Test
    public void  getCountOfBookWithTitleTest(){
        int res = bookDao.getCountOfBookWithTitle("bla-bla-bla");
        assertEquals(0,res);
        res = bookDao.getCountOfBookWithTitle(firstBook.getTitle());
        assertEquals(1,res);
        res = bookDao.getCountOfBookWithTitle(secondBook.getTitle());
        assertEquals(1,res);
    }

    @Test
    public void deleteBookByIdTest(){
        bookDao.deleteBookById(firstBook.getId());
        List<Book> books = bookDao.getAllBooks();
        Assert.assertEquals(COUNT_OF_BOOKS-1,books.size());

        thrown.expect(EmptyResultDataAccessException.class);
        bookDao.getBookById(firstBook.getId());

    }
    @Test
    public void deleteBookByUnexistIdTest(){
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(BookDaoErrors.BOOK_ISNT_EXIST);
        bookDao.deleteBookById(COUNT_OF_BOOKS+1);
    }

    @Test
    public void updateBookTest(){
        Book testBook = new Book(firstBook.getId(),"Bla bla bla book",99,LocalDate.now(),"Ru");
        int changedRows = bookDao.updateBook(testBook);
        assertEquals(1,changedRows);

        Book actualBook = bookDao.getBookById(firstBook.getId());

        assertEquals(testBook,actualBook);
    }

    @Test
    public void updateUnexistBookTest(){
        Book testBook = new Book
                (COUNT_OF_BOOKS+1, "Bla bla bla book",99, LocalDate.now(),"Ru");
        int changedRows = bookDao.updateBook(testBook);
        assertEquals(0,changedRows);
    }
    @Test
    public void addBookTest(){
        Book testBook = new Book(null,"New book",99,LocalDate.now(),"Ru");
        bookDao.addBook(testBook);
        testBook.setId(COUNT_OF_BOOKS+1);
        List<Book> books = bookDao.getAllBooks();
        assertEquals(COUNT_OF_BOOKS+1,books.size());
        Book actualBook = bookDao.getBookByTitle(testBook.getTitle());
        assertNotNull(actualBook);
        assertEquals(testBook,actualBook);

        //Try to add duplicate book(because id counter increments even when you try to add book)
        thrown.expect(DuplicateKeyException.class);
        bookDao.addBook(firstBook);
    }
    @Test
    public void getBooksFromPeriodTest(){

        List<Book> books = bookDao.getBooksFromPeriod(firstBook.getReleaseDate(),secondBook.getReleaseDate());
        assertEquals(COUNT_OF_BOOKS,books.size());
        books = bookDao.getBooksFromPeriod(firstBook.getReleaseDate(),LocalDate.now());
        assertEquals(COUNT_OF_BOOKS,books.size());

        books = bookDao.getBooksFromPeriod(firstBook.getReleaseDate(),secondBook.getReleaseDate().minusDays(1));
        assertEquals(COUNT_OF_BOOKS-1,books.size());
        for (Book book:books) {
            Assert.assertNotEquals(secondBook,book);
        }

        books = bookDao.getBooksFromPeriod(LocalDate.parse("2015-01-01"),secondBook.getReleaseDate());
        assertEquals(COUNT_OF_BOOKS-1,books.size());
        for (Book book:books) {
            Assert.assertNotEquals(firstBook,book);
        }
    }
    @Test
    public void getBooksByAuthorTest(){
        List<Book> books = bookDao.getBooksByAuthor(firstAuthor);
        assertNotNull(books);
        assertEquals(2,books.size());
        assertNotEquals(-1, books.indexOf(firstBook));
        assertNotEquals(-1,books.indexOf(thirdBook));

        books = bookDao.getBooksByAuthor(secondAuthor);
        assertNotNull(books);
        assertEquals(2,books.size());
        assertNotEquals(-1, books.indexOf(secondBook));
        assertNotEquals(-1,books.indexOf(fourthBook));
    }
    @Test
    public void getNoBooksByAuthor(){
        List<Book> books = bookDao.getBooksByAuthor(thirdAuthor);
        assertEquals(0,books.size());
    }


}

