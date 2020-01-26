package ru.mail.druk_aleksandr.app.service;

import java.util.List;

import ru.mail.druk_aleksandr.app.service.model.AddUserDTO;
import ru.mail.druk_aleksandr.app.service.model.UserDTO;
import ru.mail.druk_aleksandr.app.service.model.UserInformationDTO;

public interface UserService {
    public void add(List<AddUserDTO> addUserDTO);

    List<UserDTO> findAll();

    UserDTO get(int id);

    void update(UserDTO userDTO, UserInformationDTO userInformationDTO);

    void deleteUser(int id);

    void deleteUserInformation(int id);
}
