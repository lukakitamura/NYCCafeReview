// RegisterServlet.java
package com.cafereview.servlet.auth;

import com.cafereview.dao.UserDAO;
import com.cafereview.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        try {
            if (userDAO.findByUsername(username) != null) {
                request.setAttribute("error", "Username already exists");
                request.getRequestDispatcher("/WEB-INF/jsp/auth/register.jsp").forward(request, response);
                return;
            }

            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);

            if (userDAO.create(user)) {
                response.sendRedirect(request.getContextPath() + "/login");
            } else {
                throw new ServletException("Failed to create user");
            }
        } catch (Exception e) {
        	throw new ServletException("Registration failed", e); 
	}
    }
}
