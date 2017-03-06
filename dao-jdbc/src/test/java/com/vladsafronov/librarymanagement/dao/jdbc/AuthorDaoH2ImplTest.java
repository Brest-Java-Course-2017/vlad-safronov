package com.vladsafronov.librarymanagement.dao.jdbc;

import com.vladsafronov.librarymanagement.dao.api.AuthorDao;
import com.vladsafronov.librarymanagement.dao.api.BookDao;
import com.vladsafronov.librarymanagement.model.Author;
import com.vladsafronov.librarymanagement.model.Book;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by vlad on 4.3.17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:test-spring-config.xml"})
@Transactional
public class AuthorDaoH2ImplTest {

    private static final Author firstAuthor = new Author(1,"George","Orwell", LocalDate.parse("2010-01-01"));
    private static final Author secondAuthor = new Author(2, "Aldous","Haxley",LocalDate.parse("2009-01-01"));
    private static final Author thirdAuthor = new Author(3,  "Vlad","Safronov",LocalDate.parse("1998-08-22"));

    public static final Book firstBook = new Book(1,"1984",89,LocalDate.parse("2014-01-01"),"Eng");
    public static final Book secondBook = new Book(2,"Brave New World",85,LocalDate.parse("2016-01-02"),"Eng");
    public static final Book thirdBook = new Book (3,"The Road to Wigan Pier",50,LocalDate.parse("2015-01-01"),"Eng");
    public static final Book fourthBook = new Book(4,"The Doors of Perception",54,LocalDate.parse("2015-02-02"),"Eng");


    private static final int COUNT_OF_AUTHORS = 3;
    public static final int COUNT_OF_BOOKS = 4;


    @Autowired
    AuthorDao authorDao;
    @Autowired
    BookDao bookDao;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test
    public void getAuthorById(){
        Author testAuthor = authorDao.getAuthorById(1);
        assertNotNull(testAuthor);
        assertEquals(testAuthor,firstAuthor);

        testAuthor = authorDao.getAuthorById(2);
        assertNotNull(testAuthor);
        assertEquals(testAuthor,secondAuthor);

        testAuthor = authorDao.getAuthorById(3);
        assertNotNull(testAuthor);
        assertEquals(testAuthor,thirdAuthor);

    }
    @Test(expected = EmptyResultDataAccessException.class)
    public void getAuthorByNotValidId(){
        authorDao.getAuthorById(0);
    }

    @Test
    public void getAllAuthors(){
        List<Author> authors = authorDao.getAllAuthors();
        assertNotNull(authors);
        assertEquals(COUNT_OF_AUTHORS,authors.size());

        assertEquals(firstAuthor,authors.get(0));
        assertEquals(secondAuthor,authors.get(1));
        assertEquals(thirdAuthor,authors.get(2));
    }

    @Test
    public void getCountOfAuthors(){
        int count = authorDao.getCountOfAuthorsByNameAndSurname(firstAuthor);
        assertEquals(1,count);
        Author author = new Author
                (firstAuthor.getId(),firstAuthor.getName(),"blabla",firstAuthor.getBirthDate());
        count = authorDao.getCountOfAuthorsByNameAndSurname(author);
        assertEquals(0,count);

        author = new Author
                (firstAuthor.getId(),"blabla",firstAuthor.getSurname(),firstAuthor.getBirthDate());
        count = authorDao.getCountOfAuthorsByNameAndSurname(author);
        assertEquals(0,count);

        count = authorDao.getCountOfAuthorsByNameAndSurname(secondAuthor);
        assertEquals(1,count);
        count = authorDao.getCountOfAuthorsByNameAndSurname(thirdAuthor);
        assertEquals(1,count);
        author = new Author(null,"Blabla","Blabla",LocalDate.now());
        count = authorDao.getCountOfAuthorsByNameAndSurname(author);
        assertEquals(0,count);
    }

    @Test
    public void addAuthor(){
        Author author = new Author(COUNT_OF_AUTHORS+1,"Blabla","Blabla",LocalDate.now());
        authorDao.addAuthor(author);
        Author actual = authorDao.getAuthorById(COUNT_OF_AUTHORS+1);
        assertNotNull(actual);
        assertEquals(author,actual);
    }

    @Test
    public void getAuthorByBook(){
        Author author = authorDao.getAuthorByBook(firstBook);
        assertNotNull(author);
        assertEquals(firstAuthor,author);

        author = authorDao.getAuthorByBook(secondBook);
        assertNotNull(author);
        assertEquals(secondAuthor,author);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getAuthorByUnexistedBook(){
        Book book = new Book(COUNT_OF_BOOKS+1,"Blabla",1,LocalDate.now(),"Ru");
        authorDao.getAuthorByBook(book);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void deleteAuthorAndCheck(){
        authorDao.deleteAuthorById(3);
        authorDao.getAuthorById(3);
    }
    @Test
    public void deleteAuthorWithBiggerThenCountOfAuthorsId(){
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(DaoErrorMessages.ELEMENT_WITH_SUCH_ID_ISNT_EXIST);

        authorDao.deleteAuthorById(COUNT_OF_AUTHORS+1);
    }


    @Test(expected = DataIntegrityViolationException.class)
    public void deleteAuthorWithBooks(){
        authorDao.deleteAuthorById(1);
    }

    @Test
    public void getCountOfAuthorsBooks(){
        int count = authorDao.getCountOfAuthorsBooks(firstAuthor);
        assertEquals(2,count);

        count = authorDao.getCountOfAuthorsBooks(secondAuthor);
        assertEquals(2,count);

        count = authorDao.getCountOfAuthorsBooks(thirdAuthor);
        assertEquals(0,count);
    }

    @Test
    public void updateAuthor(){
        Author author = new Author
                (firstAuthor.getId(),"Blabla","Blabla",LocalDate.now());
        int count = authorDao.updateAuthor(author);
        assertEquals(1,count);
        Author actualAuthor = authorDao.getAuthorById(firstAuthor.getId());
        assertEquals(author,actualAuthor);

    }

    @Test
    public void updateUnexistAuthor(){
        Author author = new Author
                (COUNT_OF_AUTHORS+1,"Blabla","Blabla",LocalDate.now());
        int count = authorDao.updateAuthor(author);
        assertEquals(0,count);
    }

    @Test
    public void getAverageRatingOfAuthorsBooks(){
        double delta = 0.001;
        double average = (firstBook.getRating()+thirdBook.getRating())/2;
        double actual = authorDao.getAverageRatingOfAuthorsBooks(firstAuthor);
        assertEquals(average,actual,delta);
        average = (secondBook.getRating()+fourthBook.getRating())/2;
        actual = authorDao.getAverageRatingOfAuthorsBooks(secondAuthor);
        assertEquals(average,actual,delta);


        /*
        FIXME 0 books or Unexist author? Make it see difference
        average = 0;
        actual = authorDao.getAverageRatingOfAuthorsBooks(thirdAuthor);
        assertEquals(average,actual,delta);*/
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAverageBookRatingOnUnexistAuthor(){
      double count = authorDao.getAverageRatingOfAuthorsBooks
                (new Author(COUNT_OF_AUTHORS+1,"Bla","Bla",LocalDate.now()));
    }

    @Test
    public void getCountOfAllAuthors(){
        assertEquals(COUNT_OF_AUTHORS,authorDao.getCountOfAllAuthors());
    }

    @Test
    public void getCountOfAllAuthorsIfHasNoAuthors(){
        for (int i = 1; i <= COUNT_OF_BOOKS; i++) {
            bookDao.deleteBookById(i);
        }
        for (int i = 1; i <= COUNT_OF_AUTHORS; i++) {
            authorDao.deleteAuthorById(i);
        }
        assertEquals(0,authorDao.getCountOfAllAuthors());
    }

}