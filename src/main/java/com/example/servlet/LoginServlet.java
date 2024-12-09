package com.example.servlet;

import com.example.dao.UserDAO;
import com.example.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

import at.favre.lib.crypto.bcrypt.BCrypt;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        try {
            userDAO.getUserByUsername(username).ifPresentOrElse(user -> {
                if (BCrypt.verifyer().verify(password.toCharArray(),
                        user.getPassword().toCharArray()).verified) {
                    HttpSession session = req.getSession();
                    session.setAttribute("user", user);
                    try {
                        resp.sendRedirect("reviews");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    try {
                        req.setAttribute("error", "Invalid credentials");
                        req.getRequestDispatcher("login.jsp").forward(req, resp);
                    } catch (ServletException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, () -> {
                try {
                    req.setAttribute("error", "User not found");
                    req.getRequestDispatcher("login.jsp").forward(req, resp);
                } catch (ServletException | IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
}