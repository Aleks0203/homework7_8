package ru.mail.druk_aleksandr.app.repository.impl;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ru.mail.druk_aleksandr.app.repository.model.UserInformation;
import ru.mail.druk_aleksandr.app.repository.UserRepository;
import ru.mail.druk_aleksandr.app.repository.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserRepositoryImpl extends GeneralRepositoryImpl<User> implements UserRepository {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private static UserRepository instance;

    private UserRepositoryImpl() {
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepositoryImpl();
        }
        return instance;
    }

    @Override
    public User add(Connection connection, User user) throws SQLException {
        try (
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO user(username, password, is_active, age) VALUES (?,?,?,?)",
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setBoolean(3, user.getActive());
            statement.setInt(4, user.getAge());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            return user;
        }
    }

    @Override
    public void delete(Connection connection, Serializable id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM user WHERE id=?")) {
            statement.setInt(1, (Integer) id);
            statement.execute();
        }
    }

    @Override
    public void update(Connection connection, User user, UserInformation userInformation) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE user AS u LEFT JOIN user_information AS ui ON u.id = ui.user_id SET u.username=?, u.password=?, u.age=?, u.is_active=?, ui.telephone=?, ui.address=? WHERE u.id=?")) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setInt(3, user.getAge());
            statement.setBoolean(4, user.getActive());
            statement.setString(5, userInformation.getTelephone());
            statement.setString(6, userInformation.getAddress());
            statement.setInt(7, user.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public List<User> findAll(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT u.id, username, password, age, is_active, ui.telephone, ui.address FROM user u LEFT JOIN user_information ui on u.id = ui.user_id")) {
            List<User> users = new ArrayList<>();
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    User person = getUser(rs);
                    users.add(person);
                }
                return users;
            }
        }
    }

    @Override
    public User get(Connection connection, Serializable id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT u.id, username, password, age, is_active, ui.telephone, ui.address FROM user u LEFT JOIN user_information ui on u.id = ui.user_id where u.id=?")) {
            statement.setInt(1, (Integer) id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return getUser(rs);
                }
            }
            return null;
        }
    }

    private User getUser(ResultSet rs) throws SQLException {
        User user = new User();
        int id = rs.getInt("id");
        user.setId(id);
        String username = rs.getString("username");
        user.setUsername(username);
        String password = rs.getString("password");
        user.setPassword(password);
        Integer age = rs.getInt("age");
        user.setAge(age);
        Boolean isActive = rs.getBoolean("is_active");
        user.setActive(isActive);
        UserInformation userInformation = new UserInformation();
        userInformation.setTelephone(rs.getString("telephone"));
        userInformation.setAddress(rs.getString("address"));
        user.setUserInformation(userInformation);
        return user;
    }
}
