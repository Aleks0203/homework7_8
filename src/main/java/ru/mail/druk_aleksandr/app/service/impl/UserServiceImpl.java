package ru.mail.druk_aleksandr.app.service.impl;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import ru.mail.druk_aleksandr.app.repository.ConnectionRepository;
import ru.mail.druk_aleksandr.app.repository.UserInformationRepository;
import ru.mail.druk_aleksandr.app.repository.UserRepository;
import ru.mail.druk_aleksandr.app.repository.impl.ConnectionRepositoryImpl;
import ru.mail.druk_aleksandr.app.repository.impl.UserInformationRepositoryImpl;
import ru.mail.druk_aleksandr.app.repository.impl.UserRepositoryImpl;
import ru.mail.druk_aleksandr.app.repository.model.User;
import ru.mail.druk_aleksandr.app.repository.model.UserInformation;
import ru.mail.druk_aleksandr.app.service.UserService;
import ru.mail.druk_aleksandr.app.service.model.AddUserDTO;
import ru.mail.druk_aleksandr.app.service.model.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.mail.druk_aleksandr.app.service.model.UserInformationDTO;

public class UserServiceImpl implements UserService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private static UserService instance;
    private UserRepository userRepository;
    private UserInformationRepository userInformationRepository;
    private ConnectionRepository connectionRepository;

    private UserServiceImpl(
            ConnectionRepository connectionRepository,
            UserRepository userRepository,
            UserInformationRepository userInformationRepository
    ) {
        this.connectionRepository = connectionRepository;
        this.userRepository = userRepository;
        this.userInformationRepository = userInformationRepository;
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserServiceImpl(
                    ConnectionRepositoryImpl.getInstance(),
                    UserRepositoryImpl.getInstance(),
                    UserInformationRepositoryImpl.getInstance());
        }
        return instance;
    }

    @Override
    public void add(List<AddUserDTO> addUserDTO) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                for (AddUserDTO user : addUserDTO) {
                    User databaseUser = convertDTOToDatabaseUser(user);
                    User addedUser = userRepository.add(connection, databaseUser);
                    databaseUser.getUserInformation().setUserId(addedUser.getId());
                    userInformationRepository.add(connection, databaseUser.getUserInformation());
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public List<UserDTO> findAll() {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                List<User> people = userRepository.findAll(connection);
                List<UserDTO> userDTOList = convertDatabaseUserToDTO(people);
                connection.commit();
                return userDTOList;
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }

    @Override
    public UserDTO get(int id) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                UserDTO userDTO = new UserDTO();
                User user = convertDTOToUser(userDTO);
                user = userRepository.get(connection, id);
                UserInformation userInformation = user.getUserInformation();
                connection.commit();
                return convertUserUserInformation(user, userInformation);
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void update(UserDTO userDTO, UserInformationDTO userInformationDTO) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                User user = convertDTOToUser(userDTO);
                UserInformation userInformation = convertDTOToUserInformation(userInformationDTO);
                userRepository.update(connection, user, userInformation);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void deleteUser(int id) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                userRepository.delete(connection, id);
                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void deleteUserInformation(int id) {
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            try {
                userInformationRepository.delete(connection, id);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    private User convertDTOToDatabaseUser(AddUserDTO addUserDTO) {
        User databaseUser = new User();
        databaseUser.setUsername(addUserDTO.getUsername());
        databaseUser.setPassword(addUserDTO.getPassword());
        databaseUser.setAge(addUserDTO.getAge());
        databaseUser.setActive(addUserDTO.isActive());
        UserInformation userInformation = new UserInformation();
        userInformation.setTelephone(addUserDTO.getTelephone());
        userInformation.setAddress(addUserDTO.getAddress());
        databaseUser.setUserInformation(userInformation);
        return databaseUser;
    }

    private User convertDTOToUser(UserDTO userDTO) {
        User databaseUser = new User();
        databaseUser.setId(userDTO.getId());
        databaseUser.setUsername(userDTO.getUsername());
        databaseUser.setPassword(userDTO.getPassword());
        databaseUser.setAge(userDTO.getAge());
        databaseUser.setActive(userDTO.getActive());
        UserInformation userInformation = new UserInformation();
        userInformation.setTelephone(userDTO.getTelephone());
        userInformation.setAddress(userDTO.getAddress());
        databaseUser.setUserInformation(userInformation);
        return databaseUser;
    }

    private UserInformation convertDTOToUserInformation(UserInformationDTO userInformationDTO) {
        UserInformation userInformation = new UserInformation();
        userInformation.setTelephone(userInformationDTO.getTelephone());
        userInformation.setAddress(userInformationDTO.getAddress());
        return userInformation;
    }

    private List<UserDTO> convertDatabaseUserToDTO(List<User> people) {
        return people.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    private UserDTO convert(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setAge(user.getAge());
        userDTO.setActive(user.getActive());
        if (user.getUserInformation() != null) {
            userDTO.setTelephone(user.getUserInformation().getTelephone());
            userDTO.setAddress(user.getUserInformation().getAddress());
        }
        return userDTO;
    }

    private UserDTO convertUserUserInformation(User user, UserInformation userInformation) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setAge(user.getAge());
        userDTO.setActive(user.getActive());
        UserInformationDTO userInformationDTO = new UserInformationDTO();
        userDTO.setTelephone(userInformation.getTelephone());
        userDTO.setAddress(userInformation.getAddress());
        userInformationDTO.setTelephone(userInformation.getTelephone());
        userInformationDTO.setAddress(userInformation.getAddress());
        userDTO.setUserInformationDTO(userInformationDTO);
        return userDTO;
    }
}
