// ReviewServlet.java
package com.cafereview.servlet;

import com.cafereview.dao.ReviewDAO;
import com.cafereview.model.Review;
import com.cafereview.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/review")
public class ReviewServlet extends HttpServlet {
    private ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            User user = (User) session.getAttribute("user");
            Review review = new Review();
            
            review.setCafeId(Integer.parseInt(request.getParameter("cafeId")));
            review.setUserId(user.getUserId());
            review.setRating(Integer.parseInt(request.getParameter("rating")));
            review.setComment(request.getParameter("comment"));

            if (reviewDAO.create(review)) {
                response.sendRedirect(request.getContextPath() + "/cafes/" + review.getCafeId());
            } else {
                throw new ServletException("Failed to create review");
            }
        } catch (Exception e) {
            throw new ServletException("Error processing review submission", e);
        }
    }
}
