// CafeServlet.java
package com.cafereview.servlet;

import com.cafereview.dao.CafeDAO;
import com.cafereview.dao.ReviewDAO;
import com.cafereview.model.Cafe;
import com.cafereview.model.Review;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/cafes/*")
public class CafeServlet extends HttpServlet {
    private CafeDAO cafeDAO = new CafeDAO();
    private ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                // List all cafes
                List<Cafe> cafes = cafeDAO.findAll();
                request.setAttribute("cafes", cafes);
                request.getRequestDispatcher("/WEB-INF/jsp/cafe/list.jsp").forward(request, response);
            } else {
                // Show specific cafe
                int cafeId = Integer.parseInt(pathInfo.substring(1));
                Cafe cafe = cafeDAO.findById(cafeId);
                if (cafe != null) {
                    List<Review> reviews = reviewDAO.findByCafeId(cafeId);
                    double avgRatings = reviewDAO.getAverageRatings(cafeId);
                    
                    request.setAttribute("cafe", cafe);
                    request.setAttribute("reviews", reviews);
                    request.setAttribute("avgRatings", avgRatings);
                    request.getRequestDispatcher("/WEB-INF/jsp/cafe/details.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            }
        } catch (Exception e) {
            throw new ServletException("Error processing cafe request", e);
        }
    }
}
