package com.vladsafronov.librarymanagement.rest;

import com.vladsafronov.librarymanagement.model.Book;
import com.vladsafronov.librarymanagement.service.api.BookService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Book rest controller
 */

@RestController
public class BookController {

    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    BookService bookService;

    @RequestMapping(value ="/books")
    public List<Book> getAllBooks(){
        LOGGER.debug("getAllBooks()");
        return bookService.getAllBooks();
    }

    @RequestMapping(value = "/book/{id}")
    public Book getBookById(@PathVariable Integer id){
        LOGGER.debug("getBookById(): id = "+id);
        return bookService.getBookById(id);
    }

    @RequestMapping(value = "/book")
    public Book getBookByTitle(@RequestParam(name = "title") String title){
        LOGGER.debug("getBookByTitle(): title = "+title);
        return bookService.getBookByTitle(title);
    }

    @RequestMapping(value="/book/{id}/delete")
    public void deleteBookById(@PathVariable Integer id){
        LOGGER.debug("deleteBookById(): id = " + id);
        bookService.deleteBookById(id);
    }

    @RequestMapping(value="/book/period")
    public List<Book> getBooksFromPeriod
            (@RequestParam(name="from") String from,
             @RequestParam(name="to")String to ){
        LOGGER.debug("getBookFromPeriod(): from="+from+" to="+to);
        return bookService.getBooksFromPeriod(LocalDate.parse(from),LocalDate.parse(to));
    }

}
