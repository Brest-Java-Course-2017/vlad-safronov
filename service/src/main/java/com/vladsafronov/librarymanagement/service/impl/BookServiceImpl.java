package com.vladsafronov.librarymanagement.service.impl;

import com.vladsafronov.librarymanagement.dao.api.BookDao;
import com.vladsafronov.librarymanagement.model.Author;
import com.vladsafronov.librarymanagement.model.Book;
import com.vladsafronov.librarymanagement.service.api.BookService;
import com.vladsafronov.librarymanagement.service.impl.integration.ServiceErrorMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static com.vladsafronov.librarymanagement.service.impl.integration.Validation.validateId;

/**
 * BookService implementation
 */
public class BookServiceImpl implements BookService {

    private static final Logger LOGGER = LogManager.getLogger();

    BookDao bookDao;

    public static final LocalDate MIN_DATE = LocalDate.parse("0000-01-01");

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public List<Book> getAllBooks() {
        LOGGER.debug("getAllBooks()");

        return bookDao.getAllBooks();
    }

    @Override
    public List<Book> getBooksByAuthorId(Integer authorId) {
        LOGGER.debug("getBooksByAuthorId(): authorId="+authorId);
        //FIXME If author not exist return empty list

        Assert.notNull(authorId);
        validateId(authorId);

        return bookDao.getBooksByAuthorId(authorId);
    }

    @Override
    public Book getBookById(Integer id) {
        LOGGER.debug("getBookById(): "+id);

        Assert.notNull(id);
        validateId(id);

        if(id>bookDao.getCountOfAllBooks()) {
            throw new IllegalArgumentException(ServiceErrorMessages.ID_IS_NOT_IN_ACCEPTABLE_RANGE);
        }

        return bookDao.getBookById(id);
    }

    @Override
    public List<Book> getBooksFromPeriod(LocalDate from, LocalDate to) {
        LOGGER.debug("getBooksFromPeriod(): from "+from+" to "+to);

        if(from == null){
            from = MIN_DATE;
        }

        if(to == null){
            to = LocalDate.now();
        }

        Assert.isTrue(from.compareTo(LocalDate.now())<0);
        Assert.isTrue(from.compareTo(to)<0);
        Assert.isTrue(!(from.equals(to)));

        return bookDao.getBooksFromPeriod(from,to);
    }

    @Override
    public Book getBookByTitle(String title) {
        LOGGER.debug("getBookByTitle(): "+title);

        Assert.hasText(title);
        return bookDao.getBookByTitle(title);
    }

    @Override
    public int updateBook(Book book) {
        LOGGER.debug("updateBook():"+book);

        Assert.notNull(book);
        Assert.notNull(book.getId());
        Assert.hasText(book.getTitle());
        Assert.notNull(book.getRating());
        Assert.notNull(book.getReleaseDate());
        Assert.hasText(book.getLanguage());

        return bookDao.updateBook(book);
    }

    @Override
    public void deleteBookById(Integer id) {
        LOGGER.debug("deleteBookById(): id = "+id);

        Assert.notNull(id);
        validateId(id);

        bookDao.deleteBookById(id);
    }

    @Override
    public void addBook(Book book) {
        LOGGER.debug("addBook(): "+book);

        Assert.notNull(book);
        Assert.isNull(book.getId());
        Assert.hasText(book.getTitle());
        Assert.notNull(book.getRating());
        Assert.notNull(book.getReleaseDate());
        Assert.hasText(book.getLanguage());

        bookDao.addBook(book);
    }

}
