package com.epam.test.dao;

import com.epam.test.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

/**
 * Dao implementation.
 */
public class UserDaoImpl implements UserDao {

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserDaoImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<User> getAllUsers() {
        String getAllUsersSql = "select user_id, login, password, description from app_user";
        return jdbcTemplate.query(getAllUsersSql, new UserRowMapper());
    }

    @Override
    public User getUserById(Integer userId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("p_user_id", userId);
        User user = null;
        try {
             user = namedParameterJdbcTemplate.queryForObject(
                    "select user_id, login, password, description from app_user" +
                            " where user_id = :p_user_id", namedParameters, new UserRowMapper());
        }catch(EmptyResultDataAccessException e){

        }
        return user;
    }

    @Override
    public Integer addUser(User user) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(user);
        String addUserSql = "INSERT INTO app_user (login,password,description) " +
                            "VALUES (:Login,:Password,:Description)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(addUserSql, namedParameters, keyHolder);
        return keyHolder.getKey().intValue();
    }

    @Override
    public void updateUser(User user) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(user);
        String updateUserSql = "UPDATE app_user SET " +
                "login=:login,password=:password,description=:description " +
                "WHERE user_id=:userId";
        namedParameterJdbcTemplate.update(updateUserSql,namedParameters);
    }

    @Override
    public void deleteUser(Integer userId) {
        Object[] params = {userId};
        int[] types = {Types.BIGINT};
        String deleteUserByIdSql = "DELETE FROM app_user WHERE user_id=?";
        jdbcTemplate.update(deleteUserByIdSql,params,types);
    }

    private class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            User user = new User(
                    resultSet.getInt("user_id"),
                    resultSet.getString("login"),
                    resultSet.getString("password"),
                    resultSet.getString("description"));
            return user;
        }
    }
}
