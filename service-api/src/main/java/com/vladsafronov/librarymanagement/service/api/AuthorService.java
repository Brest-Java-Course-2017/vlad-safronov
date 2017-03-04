package com.vladsafronov.librarymanagement.service.api;

import com.vladsafronov.librarymanagement.model.Author;
import com.vladsafronov.librarymanagement.model.Book;


import java.util.List;

/**
 * Author service interface
 */
public interface AuthorService {
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
     * Get author who was wrote this book
     *
     * @param book book object
     * @return author who was wrote this book
     */
    Author getAuthorByBook(Book book);


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
