package ru.mail.druk_aleksandr.app.repository.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import ru.mail.druk_aleksandr.app.repository.UserInformationRepository;
import ru.mail.druk_aleksandr.app.repository.model.UserInformation;

public class UserInformationRepositoryImpl implements UserInformationRepository {

    private static UserInformationRepository instance;

    private UserInformationRepositoryImpl() {
    }

    public static UserInformationRepository getInstance() {
        if (instance == null) {
            instance = new UserInformationRepositoryImpl();
        }
        return instance;
    }
    @Override
    public UserInformation add(Connection connection, UserInformation userInformation) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO user_information(user_id, address, telephone) VALUES (?,?,?)")){
            statement.setInt(1, userInformation.getUserId());
            statement.setString(2, userInformation.getAddress());
            statement.setString(3, userInformation.getTelephone());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user information failed, no rows affected.");
            }
            return userInformation;
        }
    }
    @Override
    public void delete(Connection connection, Serializable id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM user_information WHERE user_id=?")) {
            statement.setInt(1, (Integer) id);
            statement.execute(); }
    }

    @Override
    public List<UserInformation> findAll(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public UserInformation get(Connection connection, Serializable id) throws SQLException {
        return null;
    }

    private UserInformation getUserInformation(ResultSet rs) throws SQLException {
        UserInformation userInformation = new UserInformation();
        userInformation.setUserId(rs.getInt("user_id"));
        userInformation.setAddress(rs.getString("address"));
        userInformation.setTelephone(rs.getString("telephone"));
        return userInformation;
    }
}
