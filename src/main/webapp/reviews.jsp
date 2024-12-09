<%--
  Created by IntelliJ IDEA.
  User: luka
  Date: 12/9/24
  Time: 1:39 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Cafe Reviews</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container">
    <div class="header">
        <h1>Cafe Reviews</h1>
        <div class="user-info">
            Welcome, ${sessionScope.user.username}!
            <a href="logout" class="logout-btn">Logout</a>
        </div>
    </div>

    <div class="review-form">
        <h2>Write a Review</h2>
        <form action="reviews" method="post">
            <div class="form-group">
                <label for="cafeName">Cafe Name:</label>
                <input type="text" id="cafeName" name="cafeName" required>
            </div>
            <div class="form-group">
                <label for="rating">Rating:</label>
                <select id="rating" name="rating" required>
                    <option value="5">5 - Excellent</option>
                    <option value="4">4 - Very Good</option>
                    <option value="3">3 - Good</option>
                    <option value="2">2 - Fair</option>
                    <option value="1">1 - Poor</option>
                </select>
            </div>
            <div class="form-group">
                <label for="reviewText">Review:</label>
                <textarea id="reviewText" name="reviewText" required></textarea>
            </div>
            <button type="submit">Submit Review</button>
        </form>
    </div>

    <div class="reviews">
        <h2>All Reviews</h2>
        <c:forEach items="${reviews}" var="review">
            <div class="review">
                <h3>${review.cafeName}</h3>
                <div class="rating">Rating: ${review.rating}/5</div>
                <div class="review-text">${review.reviewText}</div>
                <div class="review-meta">
                    Posted by ${review.username}
                    <span class="date">${review.createdAt}</span>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
