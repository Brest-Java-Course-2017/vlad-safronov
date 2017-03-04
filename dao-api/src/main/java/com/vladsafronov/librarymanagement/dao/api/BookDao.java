package com.vladsafronov.librarymanagement.dao.api;

import com.vladsafronov.librarymanagement.model.Author;
import com.vladsafronov.librarymanagement.model.Book;

import java.time.LocalDate;
import java.util.List;

/**
 * BookDao interface
 */
public interface BookDao {
    /**
     * Get all books list
     *
     * @return all books list
     */
    List<Book> getAllBooks();

    /**
     * Get books list, consisted of books, which was written by concrete author
     *
     * @param author author object
     * @return all books list,consisted of books, which was written by this author
     */
    List<Book> getBooksByAuthor(Author author);

    /**
     * Get book by id
     *
     * @param id book identifier
     * @return book
     */
    Book getBookById(Integer id);


    /**
     * Get books list consisted of books which was released in some period
     * @param from begin of period
     * @param to end of period
     * @return books list which consist books which was released in some period
     */
    List<Book> getBooksFromPeriod(LocalDate from,LocalDate to);


    /**
     * Get book by title
     *
     * @param title title of book
     * @return book
     */
    Book getBookByTitle(String title);

    /**
     * Update book
     *
     * @param book book object
     * @return count of changed records(books)
     */
    int updateBook(Book book);

    /**
     * Delete book
     *
     * @param id book identifier
     */
    void deleteBookById(Integer id);

    /**
     * Add new book
     *
     * @param book book object
     */
    void addBook(Book book);

    /**
     * Get count of book with some title
     *
     * @param title title of book
     * @return count of book with this title
     */
    int getCountOfBookWithTitle(String title);

    /**
     * Get count of all books
     *
     * @return count of all books
     */
    int getCountOfAllBooks();
}
