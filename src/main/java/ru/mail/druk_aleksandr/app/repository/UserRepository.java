package ru.mail.druk_aleksandr.app.repository;

import java.sql.Connection;
import java.sql.SQLException;

import ru.mail.druk_aleksandr.app.repository.model.User;
import ru.mail.druk_aleksandr.app.repository.model.UserInformation;

public interface UserRepository extends GeneralRepository<User> {
    void update(Connection connection, User user, UserInformation userInformation) throws SQLException;
}
