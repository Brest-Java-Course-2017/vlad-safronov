package com.vladsafronov.librarymanagement.service.impl;

import com.vladsafronov.librarymanagement.dao.api.AuthorDao;
import com.vladsafronov.librarymanagement.model.Author;
import com.vladsafronov.librarymanagement.model.Book;
import com.vladsafronov.librarymanagement.service.api.AuthorService;
import com.vladsafronov.librarymanagement.service.impl.integration.ServiceErrorMessages;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.List;

import static com.vladsafronov.librarymanagement.service.impl.integration.Validation.validateId;

/**
 * AuthorService implementation
 */
public class AuthorServiceImpl implements AuthorService{


    AuthorDao authorDao;

    public static final LocalDate DEFAULT_BIRTH_DATE = LocalDate.parse("0000-01-01");

    public void setAuthorDao(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorDao.getAllAuthors();
    }

    @Override
    public Author getAuthorById(Integer id) {

        Assert.notNull(id);
        validateId(id);

        if(id>authorDao.getCountOfAllAuthors()) {
            throw new IllegalArgumentException(ServiceErrorMessages.ID_IS_NOT_IN_ACCEPTABLE_RANGE);
        }

        return authorDao.getAuthorById(id);
    }

    @Override
    public Author getAuthorByBook(Book book) {
        Assert.notNull(book);
        Assert.notNull(book.getId());
        Assert.hasText(book.getTitle());
        validateId(book.getId());

        return authorDao.getAuthorByBook(book);
    }

    public void addAuthor(Author author) {
        Assert.notNull(author);
        Assert.isNull(author.getId());
        Assert.hasText(author.getName());
        Assert.hasText(author.getSurname());

        authorDao.addAuthor(author);
    }

    @Override
    public void deleteAuthorById(Integer id) {
        Assert.notNull(id);
        validateId(id);

        authorDao.deleteAuthorById(id);
    }

    @Override
    public int updateAuthor(Author author) {
        Assert.notNull(author);
        Assert.notNull(author.getId());
        Assert.hasText(author.getName());
        Assert.hasText(author.getSurname());
        Assert.notNull(author.getBirthDate());

        return authorDao.updateAuthor(author);
    }


    @Override
    public int getCountOfAuthorsBooks(Author author) {
        Assert.notNull(author);
        Assert.notNull(author.getId());
        validateId(author.getId());

        return authorDao.getCountOfAuthorsBooks(author);
    }

    @Override
    public double getAverageRatingOfAuthorsBooks(Author author) {
        Assert.notNull(author);
        Assert.notNull(author.getId());
        validateId(author.getId());

        return authorDao.getAverageRatingOfAuthorsBooks(author);
    }
}
