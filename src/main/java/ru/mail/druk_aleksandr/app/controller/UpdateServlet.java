package ru.mail.druk_aleksandr.app.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.mail.druk_aleksandr.app.service.UserService;
import ru.mail.druk_aleksandr.app.service.impl.UserServiceImpl;
import ru.mail.druk_aleksandr.app.service.model.UserDTO;
import ru.mail.druk_aleksandr.app.service.model.UserInformationDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class UpdateServlet extends HttpServlet {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private UserService userService = UserServiceImpl.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String address = req.getParameter("address");
        UserDTO userDTO = userService.get(id);
        userDTO.setUsername(userDTO.getUsername());
        userDTO.setPassword(userDTO.getPassword());
        userDTO.setActive(userDTO.getActive());
        userDTO.setAge(userDTO.getAge());
        UserInformationDTO userInformationDTO = userDTO.getUserInformationDTO();
        userInformationDTO.setTelephone(userInformationDTO.getTelephone());
        userInformationDTO.setAddress(address);
        userService.update(userDTO, userInformationDTO);
    }
}