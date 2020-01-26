package ru.mail.druk_aleksandr.app.controller;

import ru.mail.druk_aleksandr.app.service.TableService;
import ru.mail.druk_aleksandr.app.service.impl.TableServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class TableServlet extends HttpServlet {
    TableService tableService = TableServiceImpl.getInstance();

    @Override
    public void init() throws ServletException {
        tableService.deleteTable();
        tableService.createTable();
    }
}

