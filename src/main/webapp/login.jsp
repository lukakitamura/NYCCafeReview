<%--
  Created by IntelliJ IDEA.
  User: luka
  Date: 12/9/24
  Time: 1:37 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login - Cafe Reviews</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container">
    <h1>Login</h1>
    <% if (request.getAttribute("error") != null) { %>
    <div class="error">${error}</div>
    <% } %>
    <form action="login" method="post">
        <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" required>
        </div>
        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" required>
        </div>
        <button type="submit">Login</button>
    </form>
    <p>Don't have an account? <a href="register.jsp">Register</a></p>
</div>
</body>
</html>