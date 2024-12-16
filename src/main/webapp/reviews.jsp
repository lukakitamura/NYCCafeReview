<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Cafe Reviews</title>
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/>

    <style>
        #map {
            height: 500px;
            width: 100%;
            margin: 20px 0;
        }

        .review-form, .cafe-popup {
            margin-top: 20px;
            padding: 20px;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }

        .modal {
            display: none;
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            width: 60%;
            background-color: #fff;
            box-shadow: 0 4px 10px rgba(0,0,0,0.3);
            border-radius: 8px;
            z-index: 1000;
            padding: 20px;
        }

        .modal-header {
            font-size: 1.5em;
            margin-bottom: 10px;
        }

        .modal-close {
            position: absolute;
            top: 10px;
            right: 10px;
            cursor: pointer;
            font-size: 1.2em;
        }

        .overlay {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.6);
            z-index: 999;
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
                <label for="reviewText">Review:</label>
                <textarea id="reviewText" name="reviewText" required></textarea>
            </div>
            <input type="hidden" id="latitude" name="latitude">
            <input type="hidden" id="longitude" name="longitude">
            <button type="submit">Submit Review</button>
        </form>
    </div>
</div>

<!-- Modal for Reviews -->
<div class="overlay" id="overlay"></div>
<div class="modal" id="cafeModal">
    <div class="modal-close" onclick="closeModal()">×</div>
    <div class="modal-header" id="modalTitle">Cafe Details</div>
    <div id="modalBody">
        <!-- Dynamic content for reviews will go here -->
    </div>
</div>

<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
<script>
    const map = L.map('map').setView([40.7128, -74.0060], 13);

    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '© OpenStreetMap'
    }).addTo(map);

    // Modal handlers
    const modal = document.getElementById('cafeModal');
    const overlay = document.getElementById('overlay');

    function openModal(title, reviews) {
        document.getElementById('modalTitle').innerText = title;
        const modalBody = document.getElementById('modalBody');

        // Generate reviews content
        if (reviews.length === 0) {
            modalBody.innerHTML = "<p>No reviews available yet.</p>";
        } else {
            modalBody.innerHTML = reviews.map(review => `
                <div>
                    <strong>Rating:</strong> ${review.rating}/5<br>
                    <strong>Review:</strong> ${review.text}<br>
                    <small>By: ${review.username}</small>
                </div>
                <hr>
            `).join('');
        }

        modal.style.display = 'block';
        overlay.style.display = 'block';
    }

    function closeModal() {
        modal.style.display = 'none';
        overlay.style.display = 'none';
    }

    // Add markers for existing reviews
    <c:forEach items="${reviews}" var="review">
    L.marker([${review.latitude}, ${review.longitude}])
        .addTo(map)
        .on('click', () => {
            const reviews = [
                { rating: "${review.rating}", text: "${review.reviewText}", username: "${review.username}" }
            ];
            openModal("${review.cafeName}", reviews);
        });
    </c:forEach>

    // Add marker on map click
    let currentMarker;
    map.on('click', function (e) {
        const { lat, lng } = e.latlng;

        if (currentMarker) map.removeLayer(currentMarker);
        currentMarker = L.marker([lat, lng]).addTo(map);

        // Populate the form with location data
        document.getElementById('latitude').value = lat;
        document.getElementById('longitude').value = lng;
    });
</script>
</body>
</html>
