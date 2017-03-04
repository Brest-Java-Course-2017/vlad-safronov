package com.vladsafronov.librarymanagement.dao.jdbc;

import com.vladsafronov.librarymanagement.dao.api.AuthorDao;
import com.vladsafronov.librarymanagement.model.Author;
import com.vladsafronov.librarymanagement.model.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;


import javax.naming.OperationNotSupportedException;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * AuthorDao H2 implementation
 */
public class AuthorDaoH2Impl implements AuthorDao {

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Value("${AuthorDao.sql.getAuthorById}")
    private String GET_AUTHOR_BY_ID_SQL;
    @Value("${AuthorDao.sql.getAllAuthors}")
    private String GET_ALL_AUTHORS_SQL;
    @Value("${AuthorDao.sql.getCountOfAuthorsByNameAndSurname}")
    private String GET_COUNT_OF_AUTHORS_BY_NAME_AND_SURNAME_SQL;
    @Value("${AuthorDao.sql.addAuthor}")
    private String ADD_AUTHOR_SQL;
    @Value("${AuthorDao.sql.getAuthorByBook}")
    private String GET_AUTHOR_BY_BOOK_SQL;
    @Value("${AuthorDao.sql.deleteAuthorById}")
    private String DELETE_AUTHOR_BY_ID_SQL;
    @Value("${AuthorDao.sql.getCountOfAuthorsBooks}")
    private String GET_COUNT_OF_AUTHORS_BOOKS_SQL;
    @Value("${AuthorDao.sql.updateAuthor}")
    private String UPDATE_AUTHOR_SQL;
    @Value("${AuthorDao.sql.getAverageRatingOfAuthorsBooks}")
    private String GET_AVERAGE_RATING_OF_AUTHORS_BOOKS_SQL;
    @Value("${AuthorDao.sql.getCountOfAllAuthors}")
    private String GET_COUNT_OF_ALL_AUTHORS_SQL;

    public AuthorDaoH2Impl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Author> getAllAuthors() {
        return jdbcTemplate.query(GET_ALL_AUTHORS_SQL,new AuthorRowMapper());
    }

    @Override
    public Author getAuthorById(Integer id) {
        SqlParameterSource source = new MapSqlParameterSource("id",id);
        return namedParameterJdbcTemplate.queryForObject
                (GET_AUTHOR_BY_ID_SQL,source,new AuthorRowMapper());
    }

    @Override
    public List<Author> getAuthorsByName(String name) throws OperationNotSupportedException {
        throw new OperationNotSupportedException();

    }

    @Override
    public List<Author> getAuthorsBySurname(String surname) throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }

    @Override
    public Author getAuthorByBook(Book book) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(book);
        return namedParameterJdbcTemplate.queryForObject(GET_AUTHOR_BY_BOOK_SQL,source,new AuthorRowMapper());
    }

    @Override
    public int getCountOfAuthorsByNameAndSurname(Author author) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(author);
        return namedParameterJdbcTemplate.queryForObject(GET_COUNT_OF_AUTHORS_BY_NAME_AND_SURNAME_SQL,source,Integer.class);
    }

    @Override
    public int getCountOfAllAuthors() {
        return jdbcTemplate.queryForObject(GET_COUNT_OF_ALL_AUTHORS_SQL,Integer.class);
    }

    @Override
    public void addAuthor(Author author) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(author);
        namedParameterJdbcTemplate.update(ADD_AUTHOR_SQL,source);
    }

    @Override
    public void deleteAuthorById(Integer id) {
        SqlParameterSource source = new MapSqlParameterSource("id",id);
        namedParameterJdbcTemplate.update(DELETE_AUTHOR_BY_ID_SQL,source);
    }

    @Override
    public int updateAuthor(Author author){
        SqlParameterSource source = new BeanPropertySqlParameterSource(author);
        return namedParameterJdbcTemplate.update(UPDATE_AUTHOR_SQL,source);
    }

    @Override
    public int getCountOfAuthorsBooks(Author author) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(author);
        return namedParameterJdbcTemplate.queryForObject
                (GET_COUNT_OF_AUTHORS_BOOKS_SQL,source,Integer.class);
    }

    @Override
    public double getAverageRatingOfAuthorsBooks(Author author) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(author);
        Double average = namedParameterJdbcTemplate.queryForObject
                (GET_AVERAGE_RATING_OF_AUTHORS_BOOKS_SQL,source,Double.class);
        if(average==null) throw new IllegalArgumentException();
        return average;
    }

    private class AuthorRowMapper implements RowMapper<Author>{
        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            Author author = new Author();
            author.setId(resultSet.getInt("id"));
            author.setBirthDate(LocalDate.parse(resultSet.getString("birth_date")));
            author.setName(resultSet.getString("name"));
            author.setSurname(resultSet.getString("surname"));
            return author;
        }
    }
}
