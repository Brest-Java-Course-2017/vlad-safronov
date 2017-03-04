package com.vladsafronov.librarymanagement.dao.jdbc;

import com.vladsafronov.librarymanagement.dao.api.BookDao;
import com.vladsafronov.librarymanagement.model.Author;
import com.vladsafronov.librarymanagement.model.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * BookDao h2 implementation
 */
public class BookDaoH2Impl implements BookDao {

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Value("${BookDao.sql.getAllBooks}")
    private String GET_ALL_BOOKS_SQL;
    @Value("${BookDao.sql.getBookById}")
    private String GET_BOOK_BY_ID_SQL;
    @Value("${BookDao.sql.getBooksFromPeriod}")
    private String GET_BOOKS_FROM_PERIOD_SQL;
    @Value("${BookDao.sql.getBookByTitle}")
    private String GET_BOOK_BY_TITLE_SQL;
    @Value("${BookDao.sql.updateBook}")
    private String UPDATE_BOOK_SQL;
    @Value("${BookDao.sql.deleteBookById}")
    private String DELETE_BOOK_BY_ID_SQL;
    @Value("${BookDao.sql.deleteBookFromBookAuthor}")
    private String DELETE_BOOK_FROM_BOOK_AUTHOR_SQL;
    @Value("${BookDao.sql.addBook}")
    private String ADD_BOOK_SQL;
    @Value("${BookDao.sql.getCountOfBookWithTitle}")
    private String GET_COUNT_OF_BOOK_WITH_TITLE_SQL;
    @Value("${BookDao.sql.getBookByAuthor}")
    private String GET_BOOK_BY_AUTHOR_SQL;

    BookDaoH2Impl(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }


    public List<Book> getAllBooks() {

        return  jdbcTemplate.query(GET_ALL_BOOKS_SQL,new BookRowMapper());
    }

    public List<Book> getBooksByAuthor(Author author) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(author);
        return namedParameterJdbcTemplate.query(GET_BOOK_BY_AUTHOR_SQL,sqlParameterSource,new BookRowMapper());
    }

    public Book getBookById(Integer id) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id",id);
        return namedParameterJdbcTemplate.queryForObject
                (GET_BOOK_BY_ID_SQL,sqlParameterSource,new BookRowMapper());
    }

    @Override
    public List<Book> getBooksFromPeriod(LocalDate from, LocalDate to) {
        Map<String,Object> source = new HashMap<>();
        source.put("from",from);
        source.put("to",to);
        return namedParameterJdbcTemplate.query
                (GET_BOOKS_FROM_PERIOD_SQL,source,new BookRowMapper());
    }

    public Book getBookByTitle(String title) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("title",title);
        return namedParameterJdbcTemplate.queryForObject
                (GET_BOOK_BY_TITLE_SQL,sqlParameterSource,new BookRowMapper());
    }

    public int updateBook(Book book) {


        Map<String,Object> source = new HashMap<>();
        source.put("rating",book.getRating());
        source.put("title",book.getTitle());
        source.put("releaseDate",book.getReleaseDate());
        source.put("lang",book.getLanguage());
        source.put("id",book.getId());

        return namedParameterJdbcTemplate.update
                (UPDATE_BOOK_SQL,source);
    }

    public void deleteBookById(Integer id) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id",id);
        namedParameterJdbcTemplate.update(DELETE_BOOK_FROM_BOOK_AUTHOR_SQL,sqlParameterSource);
        if(namedParameterJdbcTemplate.update(DELETE_BOOK_BY_ID_SQL,sqlParameterSource)!=1){
            throw new IllegalArgumentException(BookDaoErrors.BOOK_ISNT_EXIST);
        }
    }

    public void addBook(Book book) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(book);
        namedParameterJdbcTemplate.update
                (ADD_BOOK_SQL,sqlParameterSource);
    }

    public int getCountOfBookWithTitle(String title) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("title",title);
        return namedParameterJdbcTemplate.queryForObject(GET_COUNT_OF_BOOK_WITH_TITLE_SQL,sqlParameterSource,Integer.class);
    }


    public static class BookRowMapper implements  RowMapper<Book>{
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            Book res = new Book();
            res.setTitle(resultSet.getString("title"));
            res.setId(resultSet.getInt("id"));
            res.setLanguage(resultSet.getString("lang"));
            res.setRating(resultSet.getInt("rating"));
            res.setReleaseDate(LocalDate.parse(resultSet.getString("release_date")));

            return res;
        }
    }
}
