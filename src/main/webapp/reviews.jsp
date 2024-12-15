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

    <!-- Leaflet CSS -->
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" 
          integrity="sha256-xodZBNTC5n17Xt2bCMJ6Et5gkA5ChD9KR8eG3kD+7Ew=" 
          crossorigin=""/>
    <!-- Leaflet JavaScript -->
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js" 
            integrity="sha256-o9N1jRaa8K2+O1HrG7xYWTWwOaFrRDs+knHsmLvZmRI=" 
            crossorigin=""></script>

    <!-- Optional custom styles for the map -->
    <style>
        #map {
            height: 500px;
            width: 100%;
            margin: 20px 0;
            border-radius: 10px;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.3);
        }
    </style>
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

    <!-- Sidebar for Cafe Listings -->
    <div id="sidebar">
        <h2>Nearby Cafes</h2>
        <ul id="cafe-list"></ul>
    </div>

    <!-- Map Section -->
    <div id="map"></div>

    <!-- Review Form -->
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
            <!-- Hidden fields for latitude and longitude -->
            <input type="hidden" id="latitude" name="latitude">
            <input type="hidden" id="longitude" name="longitude">
            <button type="submit">Submit Review</button>
        </form>
    </div>

    <!-- Display All Reviews -->
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

<!-- Map Initialization Script -->
<script>
    // Initialize the map and set its view to NYC
    const map = L.map('map').setView([40.7128, -74.0060], 13); // NYC coordinates, zoom level 13

    // Add OpenStreetMap tiles
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

    // Dynamically add markers for existing reviews
    const cafes = [
        <c:forEach items="${reviews}" var="review">
            { name: "${review.cafeName}", lat: ${review.latitude}, lng: ${review.longitude}, rating: "${review.rating}" },
        </c:forEach>
    ];

    cafes.forEach(cafe => {
        L.marker([cafe.lat, cafe.lng]).addTo(map)
            .bindPopup(`<b>${cafe.name}</b><br>Rating: ${cafe.rating}/5`);
    });

    // Add a marker on map click and populate form fields
    map.on('click', function (e) {
        const { lat, lng } = e.latlng;

        // Update the hidden fields in the review form
        document.getElementById('latitude').value = lat.toFixed(6);
        document.getElementById('longitude').value = lng.toFixed(6);

        // Add the marker to the map
        const newMarker = L.marker([lat, lng]).addTo(map);
        newMarker.bindPopup(`Latitude: ${lat.toFixed(4)}, Longitude: ${lng.toFixed(4)}`).openPopup();
    });
</script>
</body>
</html>
