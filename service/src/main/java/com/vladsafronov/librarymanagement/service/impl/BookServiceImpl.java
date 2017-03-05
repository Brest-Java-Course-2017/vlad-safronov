package com.vladsafronov.librarymanagement.service.impl;

import com.vladsafronov.librarymanagement.dao.api.BookDao;
import com.vladsafronov.librarymanagement.model.Author;
import com.vladsafronov.librarymanagement.model.Book;
import com.vladsafronov.librarymanagement.service.api.BookService;
import com.vladsafronov.librarymanagement.service.impl.integration.BookServiceErrors;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

/**
 * BookService implementation
 */
public class BookServiceImpl implements BookService {

    BookDao bookDao;

    public static final LocalDate MIN_DATE = LocalDate.parse("0000-01-01");

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDao.getAllBooks();
    }

    @Override
    public List<Book> getBooksByAuthor(Author author) {
        Assert.notNull(author);
        Assert.notNull(author.getId());
        //TODO need to check id by authorDao?
        Assert.hasText(author.getName());
        Assert.hasText(author.getSurname());

        return bookDao.getBooksByAuthor(author);
    }

    @Override
    public Book getBookById(Integer id) {
        Assert.notNull(id);

        if(id>bookDao.getCountOfAllBooks() | id<=0) {
            throw new IllegalArgumentException(BookServiceErrors.ID_IS_NOT_IN_ACCEPTABLE_RANGE);
        }

        return bookDao.getBookById(id);
    }

    @Override
    public List<Book> getBooksFromPeriod(LocalDate from, LocalDate to) {
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
        Assert.hasText(title);
        return bookDao.getBookByTitle(title);
    }

    @Override
    public int updateBook(Book book) {
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
        Assert.notNull(id);
        if(id < 0){
            throw new IllegalArgumentException
                    (BookServiceErrors.ID_IS_NOT_IN_ACCEPTABLE_RANGE);
        }
        bookDao.deleteBookById(id);
    }

    @Override
    public void addBook(Book book) {
        Assert.notNull(book);
        Assert.isNull(book.getId());
        Assert.hasText(book.getTitle());
        Assert.notNull(book.getRating());
        Assert.notNull(book.getReleaseDate());
        Assert.hasText(book.getLanguage());

        bookDao.addBook(book);
    }

}
