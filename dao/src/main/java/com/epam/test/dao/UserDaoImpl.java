package com.epam.test.dao;

import com.epam.test.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

/**
 * Dao implementation.
 */

public class UserDaoImpl implements UserDao {

    @Value("${sql.getAllUsers}")
    private String GET_ALL_USERS_SQL;

    @Value("${sql.getUserById}")
    private String GET_USER_BY_ID_SQL;

    @Value("${sql.addUser}")
    private String ADD_USER_SQL;

    @Value("${sql.updateUser}")
    private String UPDATE_USER_SQL;

    @Value("${sql.deleteUserById}")
    private String DELETE_USER_BY_ID_SQL;

    @Value("${sql.countOfUsersWithId}")
    private String COUNT_OF_USERS_WITH_ID_SQL;

    @Value("${sql.getUserByLogin}")
    private String GET_USER_BY_LOGIN_SQL;

    @Value("${sql.countOfUsersWithLogin}")
    private String COUNT_OF_USERS_WITH_LOGIN_SQL;

    private static final String USER_ID = "user_id";
    private static final String LOGIN ="login";
    private static final String PASSWORD ="password";
    private static final String DESCRIPTION="description";

    public static final String ERR_USER_IS_NOT_EXIST="User is not exist";

    private static final Logger LOGGER = LogManager.getLogger();

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserDaoImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public User getUserByLogin(String login) throws Exception {
        if(!isUserExist(login))
            throw new Exception(ERR_USER_IS_NOT_EXIST);
        SqlParameterSource namedParameters = new MapSqlParameterSource("login",login);
        return namedParameterJdbcTemplate.queryForObject
                    (GET_USER_BY_LOGIN_SQL,namedParameters,new UserRowMapper());
    }


    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query(GET_ALL_USERS_SQL, new UserRowMapper());
    }

    @Override
    public User getUserById(Integer userId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource("p_user_id", userId);
        User user = namedParameterJdbcTemplate.queryForObject(
                    GET_USER_BY_ID_SQL, namedParameters, new UserRowMapper());


        return user;
    }

    @Override
    public Integer addUser(User user) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(user);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(ADD_USER_SQL, namedParameters, keyHolder);
        return keyHolder.getKey().intValue();
    }


    @Override
    public int updateUser(User user) throws Exception {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(user);
        if(!isUserExist(user.getUserId())) throw new Exception(ERR_USER_IS_NOT_EXIST);
        return  namedParameterJdbcTemplate.update(UPDATE_USER_SQL,namedParameters);
    }
    private boolean isUserExist(Integer userId){
        int count = jdbcTemplate.queryForObject
                (COUNT_OF_USERS_WITH_ID_SQL,new Object[]{userId},Integer.class);
        return (count!=0);
    }
    public boolean isUserExist(String login){
        int count = jdbcTemplate.queryForObject
                (COUNT_OF_USERS_WITH_LOGIN_SQL,new Object[]{login},Integer.class);
        return (count!=0);
    }

    @Override
    public void deleteUser(Integer userId) throws Exception {
        if(!isUserExist(userId)) throw new Exception(ERR_USER_IS_NOT_EXIST);

        Object[] params = {userId};
        int[] types = {Types.BIGINT};

        jdbcTemplate.update(DELETE_USER_BY_ID_SQL,params,types);
    }

    private class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            User user = new User(
                    resultSet.getInt(USER_ID),
                    resultSet.getString(LOGIN),
                    resultSet.getString(PASSWORD),
                    resultSet.getString(DESCRIPTION));
            return user;
        }
    }
}
