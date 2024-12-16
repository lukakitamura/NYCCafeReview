package com.example.servlet;

import com.example.dao.ReviewDAO;
import com.example.model.Review;
import com.example.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/reviews")
public class ReviewServlet extends HttpServlet {
    private final ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<Review> reviews = reviewDAO.getAllReviews();
            req.setAttribute("reviews", reviews);
            req.getRequestDispatcher("reviews.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        Review review = new Review();
        review.setUserId(user.getId());
        review.setCafeName(req.getParameter("cafeName"));
        review.setRating(Integer.parseInt(req.getParameter("rating")));
        review.setReviewText(req.getParameter("reviewText"));
        review.setLatitude(Double.parseDouble(req.getParameter("latitude")));
        review.setLongitude(Double.parseDouble(req.getParameter("longitude")));

        try {
            reviewDAO.addReview(review);
            resp.sendRedirect("reviews");
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }
}