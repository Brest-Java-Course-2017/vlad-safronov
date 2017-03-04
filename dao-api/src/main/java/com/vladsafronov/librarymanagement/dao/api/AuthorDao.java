package com.vladsafronov.librarymanagement.dao.api;

import com.vladsafronov.librarymanagement.model.Author;
import com.vladsafronov.librarymanagement.model.Book;

import javax.naming.OperationNotSupportedException;
import java.util.List;

/**
 * AuthorDao interface
 */
public interface AuthorDao {

    /**
     * Get all authors list
     *
     * @return list with all authors
     */
    List<Author> getAllAuthors();

    /**
     * Get author with some id
     *
     * @param id author identifier
     * @return author object
     */
    Author getAuthorById(Integer id);

    /**
     * Get list of authors with some name
     *
     * @param name authors name
     * @return list of authors with this name
     */
    List<Author> getAuthorsByName(String name) throws OperationNotSupportedException;

    /**
     * Get list of authors with some surname
     *
     * @param surname authors surname
     * @return list of authors with this surname
     */
    List<Author> getAuthorsBySurname(String surname) throws OperationNotSupportedException;

    /**
     * Get author who was wrote this book
     *
     * @param book book object
     * @return author who was wrote this book
     */
    Author getAuthorByBook(Book book);

    /**
     * Get count of authors with some name and surname
     *  (name+surname must be unique)
     *
     * @param author author object
     * @return count of authors with some name or surname
     */
    int getCountOfAuthorsByNameAndSurname(Author author);

    /**
     * Get count of all authors
     *
     * @return count of all authors
     */
    int getCountOfAllAuthors();

    /**
     * Add author
     *
     * @param author author object
     */
    void addAuthor(Author author);

    /**
     * Delete author
     *
     * @param id author identifier
     */
    void deleteAuthorById(Integer id);

    /**
     * Update author
     *
     * @param author author object
     * @return count of changed record(authors)
     */
    int updateAuthor(Author author);

    /**
     * Get count of books,which was written by this author
     *
     * @param author object
     * @return count of authors books
     */
    int getCountOfAuthorsBooks(Author author);


    /**
     * Get average rating of all authors books
     *
     * @param author author object
     * @return average rating
     */
    double getAverageRatingOfAuthorsBooks(Author author);

}
