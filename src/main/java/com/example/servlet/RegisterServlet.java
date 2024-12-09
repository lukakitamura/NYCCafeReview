package com.example.servlet;

import com.example.dao.UserDAO;
import com.example.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

import at.favre.lib.crypto.bcrypt.BCrypt;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // Hash password
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        try {
            if (userDAO.getUserByUsername(username).isPresent()) {
                req.setAttribute("error", "Username already exists");
                req.getRequestDispatcher("register.jsp").forward(req, resp);
                return;
            }

            User user = new User(username, hashedPassword);
            userDAO.createUser(user);
            resp.sendRedirect("login.jsp");
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
}