package ru.mail.druk_aleksandr.app.service.impl;

import ru.mail.druk_aleksandr.app.repository.ConnectionRepository;
import ru.mail.druk_aleksandr.app.repository.TableRepository;
import ru.mail.druk_aleksandr.app.repository.impl.ConnectionRepositoryImpl;
import ru.mail.druk_aleksandr.app.repository.impl.TableRepositoryImpl;
import ru.mail.druk_aleksandr.app.service.TableService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;

public class TableServiceImpl implements TableService {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private ConnectionRepository connectionRepository = ConnectionRepositoryImpl.getInstance();
    private TableRepository tableRepository = TableRepositoryImpl.getInstance();
    private static TableService instance;

    private TableServiceImpl() {
    }

    public static TableService getInstance() {
        if (instance == null) {
            instance = new TableServiceImpl();
        }
        return instance;
    }

    @Override
    public void deleteTable() {
        try (Connection connection = connectionRepository.getConnection()) {
            tableRepository.deleteTable(connection);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void createTable() {
        try (Connection connection = connectionRepository.getConnection()) {
            tableRepository.createTable(connection);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
