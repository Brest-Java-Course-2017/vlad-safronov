package com.vladsafronov.librarymanagement.service.impl;

import com.vladsafronov.librarymanagement.dao.api.AuthorDao;
import com.vladsafronov.librarymanagement.model.Author;
import com.vladsafronov.librarymanagement.model.Book;
import com.vladsafronov.librarymanagement.service.api.AuthorService;
import com.vladsafronov.librarymanagement.service.impl.integration.ServiceErrorMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static com.vladsafronov.librarymanagement.service.impl.integration.Validation.validateId;

/**
 * AuthorService implementation
 */
public class AuthorServiceImpl implements AuthorService{

    private static final Logger LOGGER = LogManager.getLogger();

    AuthorDao authorDao;

    public static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.parse("0000-01-01");

    public void setAuthorDao(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @Override
    public List<Author> getAllAuthors() {
        LOGGER.debug("getAllAuthors()");

        return authorDao.getAllAuthors();
    }

    @Override
    public Author getAuthorById(Integer id) {
        LOGGER.debug("getAuthorById(): id = " + id);

        Assert.notNull(id);
        validateId(id);

        if(id>authorDao.getCountOfAllAuthors()) {
            throw new IllegalArgumentException(ServiceErrorMessages.ID_IS_NOT_IN_ACCEPTABLE_RANGE);
        }

        return authorDao.getAuthorById(id);
    }

    @Override
    public Author getAuthorByBook(Book book) {
        LOGGER.debug("getAuthorByBook(): "+ book);

        Assert.notNull(book);
        Assert.notNull(book.getId());
        Assert.hasText(book.getTitle());
        validateId(book.getId());

        return authorDao.getAuthorByBook(book);
    }

    public void addAuthor(Author author) {
        LOGGER.debug("addAuthor(): "+ author);

        Assert.notNull(author);
        Assert.isNull(author.getId());
        Assert.hasText(author.getName());
        Assert.hasText(author.getSurname());

        authorDao.addAuthor(author);
    }

    @Override
    public void deleteAuthorById(Integer id) {
        LOGGER.debug("deleteAuthorById(): "+ id);

        Assert.notNull(id);
        validateId(id);

        authorDao.deleteAuthorById(id);
    }

    @Override
    public int updateAuthor(Author author) {
        LOGGER.debug("updateAuthor(): "+ author);

        Assert.notNull(author);
        Assert.notNull(author.getId());
        Assert.hasText(author.getName());
        Assert.hasText(author.getSurname());
        Assert.notNull(author.getBirthDate());

        return authorDao.updateAuthor(author);
    }


    @Override
    public int getCountOfAuthorsBooks(Author author) {
        LOGGER.debug("getCountOfAuthorsBooks(): "+author);

        Assert.notNull(author);
        Assert.notNull(author.getId());
        validateId(author.getId());

        return authorDao.getCountOfAuthorsBooks(author);
    }

    @Override
    public double getAverageRatingOfAuthorsBooks(Author author) {
        LOGGER.debug("getAverageRatingOfAuthorsBooks(): "+author);

        Assert.notNull(author);
        Assert.notNull(author.getId());
        validateId(author.getId());

        return authorDao.getAverageRatingOfAuthorsBooks(author);
    }
}
