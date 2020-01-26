package ru.mail.druk_aleksandr.app.repository.impl;

import ru.mail.druk_aleksandr.app.repository.TableRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TableRepositoryImpl implements TableRepository {
    private static TableRepository instance;

    private TableRepositoryImpl() {
    }

    public static TableRepository getInstance() {
        if (instance == null) {
            instance = new TableRepositoryImpl();
        }
        return instance;
    }

    @Override
    public void deleteTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS user_information");
            statement.executeUpdate("DROP TABLE IF EXISTS user");
        }
    }

    @Override
    public void createTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE user (id INT PRIMARY KEY AUTO_INCREMENT, username VARCHAR(40) NOT NULL, password VARCHAR(40) NOT NULL, is_active BOOLEAN NOT NULL DEFAULT TRUE, age int NOT NULL)");
            statement.executeUpdate("CREATE TABLE user_information (user_id INT PRIMARY KEY NOT NULL, address VARCHAR(100), telephone VARCHAR(40), FOREIGN KEY(user_id) REFERENCES user(id))");
        }
    }
}
