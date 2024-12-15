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

    <!-- Optional custom styles for the map and modal -->
    <style>
        #map {
            height: 500px;
            width: 100%;
            margin: 20px 0;
            border-radius: 10px;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.3);
        }

        #sidebar {
            position: absolute;
            top: 20px;
            left: 20px;
            background: white;
            border: 1px solid #ccc;
            padding: 10px;
            width: 250px;
            max-height: 400px;
            overflow-y: auto;
            box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.2);
            z-index: 1000;
        }

        #sidebar ul {
            list-style: none;
            padding: 0;
        }

        #sidebar li {
            cursor: pointer;
            margin: 5px 0;
            padding: 5px;
            background: #f7f7f7;
            border: 1px solid #ddd;
            border-radius: 5px;
            transition: background 0.3s;
        }

        #sidebar li:hover {
            background: #e0e0e0;
        }

        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            justify-content: center;
            align-items: center;
            z-index: 2000;
        }

        .modal-content {
            background: white;
            padding: 20px;
            border-radius: 5px;
            max-width: 500px;
            width: 80%;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
        }

        .close-btn {
            float: right;
            font-size: 1.5rem;
            font-weight: bold;
            cursor: pointer;
            color: #333;
        }

        .close-btn:hover {
            color: #ff4444;
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

<!-- Modal for Cafe Details -->
<div id="modal" class="modal">
    <div class="modal-content">
        <span class="close-btn">&times;</span>
        <h2 id="modal-title"></h2>
        <p id="modal-details"></p>
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

    // Reference modal elements
    const modal = document.getElementById('modal');
    const modalTitle = document.getElementById('modal-title');
    const modalDetails = document.getElementById('modal-details');
    const closeBtn = document.querySelector('.close-btn');

    // Function to open the modal
    function openModal(cafe) {
        modalTitle.textContent = cafe.name;
        modalDetails.innerHTML = `
            <strong>Rating:</strong> ${cafe.rating}/5<br>
            <strong>Latitude:</strong> ${cafe.lat}<br>
            <strong>Longitude:</strong> ${cafe.lng}<br>
            <em>Additional details about the cafe go here!</em>
        `;
        modal.style.display = 'flex';
    }

    // Close modal functionality
    closeBtn.addEventListener('click', () => {
        modal.style.display = 'none';
    });

    window.addEventListener('click', (event) => {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    });

    // Add modal functionality to map markers
    cafes.forEach(cafe => {
        const marker = L.marker([cafe.lat, cafe.lng]).addTo(map);
        marker.on('click', () => {
            openModal(cafe);
        });
    });

    // Add modal functionality to sidebar list items
    cafes.forEach((cafe, index) => {
        const listItem = document.createElement('li');
        listItem.textContent = `${cafe.name} - Rating: ${cafe.rating}/5`;

        listItem.addEventListener('click', () => {
            openModal(cafe);
            map.setView([cafe.lat, cafe.lng], 15);
        });

        document.getElementById('cafe-list').appendChild(listItem);
    });
</script>
</body>
</html>
