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
    @Value("${BookDao.sql.getCountOfAllBooks}")
    private String GET_COUNT_OF_ALL_BOOKS;

    public static final String TITLE="title";
    public static final String ID="id";
    public static final String LANG="lang";
    public static final String RATING="rating";
    public static final String RELEASE_DATE="release_date";

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
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(TITLE,title);
        return namedParameterJdbcTemplate.queryForObject
                (GET_BOOK_BY_TITLE_SQL,sqlParameterSource,new BookRowMapper());
    }

    public int updateBook(Book book) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(book);

        return namedParameterJdbcTemplate.update
                (UPDATE_BOOK_SQL,source);
    }

    public void deleteBookById(Integer id) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(ID,id);
        namedParameterJdbcTemplate.update(DELETE_BOOK_FROM_BOOK_AUTHOR_SQL,sqlParameterSource);
        if(namedParameterJdbcTemplate.update(DELETE_BOOK_BY_ID_SQL,sqlParameterSource)!=1){
            throw new IllegalArgumentException(DaoErrors.ELEMENT_WITH_SUCH_ID_ISNT_EXIST);
        }
    }

    public void addBook(Book book) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(book);
        namedParameterJdbcTemplate.update
                (ADD_BOOK_SQL,sqlParameterSource);
    }

    public int getCountOfBookWithTitle(String title) {
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource(TITLE,title);
        return namedParameterJdbcTemplate.queryForObject(GET_COUNT_OF_BOOK_WITH_TITLE_SQL,sqlParameterSource,Integer.class);
    }

    public int getCountOfAllBooks(){
        return jdbcTemplate.queryForObject(GET_COUNT_OF_ALL_BOOKS,Integer.class);
    }


    public static class BookRowMapper implements  RowMapper<Book>{
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            Book res = new Book();
            res.setTitle(resultSet.getString(TITLE));
            res.setId(resultSet.getInt(ID));
            res.setLanguage(resultSet.getString(LANG));
            res.setRating(resultSet.getInt(RATING));
            res.setReleaseDate(LocalDate.parse(resultSet.getString(RELEASE_DATE)));

            return res;
        }
    }

}
