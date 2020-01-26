package ru.mail.druk_aleksandr.app.controller;

import ru.mail.druk_aleksandr.app.service.UserService;
import ru.mail.druk_aleksandr.app.service.impl.UserServiceImpl;
import ru.mail.druk_aleksandr.app.service.model.AddUserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

public class UserServlet extends HttpServlet {
    private UserService userService = UserServiceImpl.getInstance();
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        int age = Integer.parseInt(req.getParameter("age"));
        Boolean active = Boolean.valueOf(req.getParameter("is_active"));
        String telephone = req.getParameter("telephone");
        String address = req.getParameter("address");
        List<AddUserDTO> userDTOList = new ArrayList<>();
        AddUserDTO user = new AddUserDTO();
        user.setUsername(username);
        user.setPassword(password);
        user.setTelephone(telephone);
        user.setAddress(address);
        user.setAge(age);
        user.setActive(active);
        userDTOList.add(user);
        userService.add(userDTOList);
    }
}
