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
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/>
    <style>
        #map {
            height: 500px;
            width: 100%;
            margin: 20px 0;
        }
        .review-form {
            margin-top: 20px;
            padding: 20px;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
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

    <!-- Map Container -->
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
                <label for="rating">Overall Rating:</label>
                <select id="rating" name="rating" required>
                    <option value="5">5 - Excellent</option>
                    <option value="4">4 - Very Good</option>
                    <option value="3">3 - Good</option>
                    <option value="2">2 - Fair</option>
                    <option value="1">1 - Poor</option>
                </select>
            </div>

            <div class="form-group">
                <label for="wifi">WiFi Availability:</label>
                <select id="wifi" name="wifi" required>
                    <option value="EXCELLENT">Excellent - Fast & Reliable</option>
                    <option value="GOOD">Good - Works Well</option>
                    <option value="FAIR">Fair - Sometimes Slow</option>
                    <option value="POOR">Poor - Unreliable</option>
                    <option value="NONE">No WiFi Available</option>
                </select>
            </div>

            <div class="form-group">
                <label for="restroom">Restroom Availability:</label>
                <select id="restroom" name="restroom" required>
                    <option value="PUBLIC">Public - Freely Available</option>
                    <option value="CUSTOMERS">Customers Only</option>
                    <option value="KEY">Available with Key/Code</option>
                    <option value="NONE">Not Available</option>
                </select>
            </div>

            <div class="form-group">
                <label for="seating">Seating Availability:</label>
                <select id="seating" name="seating" required>
                    <option value="PLENTY">Plenty - Always Available</option>
                    <option value="MODERATE">Moderate - Usually Available</option>
                    <option value="LIMITED">Limited - Often Full</option>
                    <option value="MINIMAL">Minimal - Very Few Seats</option>
                    <option value="NONE">No Seating Available</option>
                </select>
            </div>

            <div class="form-group">
                <label for="reviewText">Review:</label>
                <textarea id="reviewText" name="reviewText" required></textarea>
            </div>

            <div class="form-group">
                <label>Selected Location:</label>
                <div id="selectedLocation">No location selected</div>
                <input type="hidden" id="latitude" name="latitude" required>
                <input type="hidden" id="longitude" name="longitude" required>
            </div>

            <div class="form-actions">
                <button type="submit">Submit Review</button>
                <button type="button" onclick="resetReviewForm()" class="secondary-btn">Clear Form</button>
            </div>

            <%--            <button type="submit">Submit Review</button>--%>
        </form>
    </div>

    <!-- Reviews List -->
    <!-- Display All Reviews -->
    <div class="reviews">
        <h2>Recent Reviews</h2>
        <c:forEach items="${reviews}" var="review">
            <div class="review">
                <h3>${review.cafeName}</h3>
                <div class="ratings-grid">
                    <div class="rating">
                        <span class="rating-label">Overall Rating:</span>
                        <span class="rating-value">${review.rating}/5</span>
                    </div>
                    <div class="rating">
                        <span class="rating-label">WiFi:</span>
                        <span class="rating-value">
                        <c:choose>
                            <c:when test="${review.wifi == 'EXCELLENT'}">Excellent - Fast & Reliable</c:when>
                            <c:when test="${review.wifi == 'GOOD'}">Good - Works Well</c:when>
                            <c:when test="${review.wifi == 'FAIR'}">Fair - Sometimes Slow</c:when>
                            <c:when test="${review.wifi == 'POOR'}">Poor - Unreliable</c:when>
                            <c:when test="${review.wifi == 'NONE'}">No WiFi Available</c:when>
                            <c:otherwise>${review.wifi}</c:otherwise>
                        </c:choose>
                    </span>
                    </div>
                    <div class="rating">
                        <span class="rating-label">Restroom:</span>
                        <span class="rating-value">
                        <c:choose>
                            <c:when test="${review.restroom == 'PUBLIC'}">Public - Freely Available</c:when>
                            <c:when test="${review.restroom == 'CUSTOMERS'}">Customers Only</c:when>
                            <c:when test="${review.restroom == 'KEY'}">Available with Key/Code</c:when>
                            <c:when test="${review.restroom == 'NONE'}">Not Available</c:when>
                            <c:otherwise>${review.restroom}</c:otherwise>
                        </c:choose>
                    </span>
                    </div>
                    <div class="rating">
                        <span class="rating-label">Seating:</span>
                        <span class="rating-value">
                        <c:choose>
                            <c:when test="${review.seating == 'PLENTY'}">Plenty - Always Available</c:when>
                            <c:when test="${review.seating == 'MODERATE'}">Moderate - Usually Available</c:when>
                            <c:when test="${review.seating == 'LIMITED'}">Limited - Often Full</c:when>
                            <c:when test="${review.seating == 'MINIMAL'}">Minimal - Very Few Seats</c:when>
                            <c:when test="${review.seating == 'NONE'}">No Seating Available</c:when>
                            <c:otherwise>${review.seating}</c:otherwise>
                        </c:choose>
                    </span>
                    </div>
                </div>
                <div class="review-text">${review.reviewText}</div>
                <div class="review-meta">
                    Posted by ${review.username}
                    <span class="date">${review.createdAt}</span>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
<script>
    // init map centered on NYC
    const map = L.map('map').setView([40.7128, -74.0060], 13);

    // OpenStreetMap tiles
    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '© OpenStreetMap'
    }).addTo(map);

    // store current marker
    let currentMarker = null;

    // click handler for map
    map.on('click', function(e) {
        // reset cafe name field
        document.getElementById('cafeName').readOnly = false;
        document.getElementById('cafeName').value = '';

        // update coordinates
        const lat = e.latlng.lat;
        const lng = e.latlng.lng;

        // remove existing marker
        if (currentMarker) {
            map.removeLayer(currentMarker);
        }

        currentMarker = L.marker([lat, lng]).addTo(map);

        // update form
        document.getElementById('latitude').value = lat;
        document.getElementById('longitude').value = lng;
        document.getElementById('selectedLocation').textContent =
            `New location selected: (${lat.toFixed(6)}, ${lng.toFixed(6)})`;
    });

    // add markers for existing reviews
    <c:forEach items="${reviews}" var="review">
    L.marker([${review.latitude}, ${review.longitude}])
        .bindPopup(`
            <div class="cafe-popup">
                <strong>${review.cafeName}</strong><br>
                <div class="cafe-ratings">
                    Overall Rating: ${review.rating}/5<br>
                    WiFi: ${review.wifi == 'EXCELLENT' ? 'Excellent - Fast & Reliable' :
                          review.wifi == 'GOOD' ? 'Good - Works Well' :
                          review.wifi == 'FAIR' ? 'Fair - Sometimes Slow' :
                          review.wifi == 'POOR' ? 'Poor - Unreliable' :
                          'No WiFi Available'}<br>
                    Restroom: ${review.restroom == 'PUBLIC' ? 'Public - Freely Available' :
                             review.restroom == 'CUSTOMERS' ? 'Customers Only' :
                             review.restroom == 'KEY' ? 'Available with Key/Code' :
                             'Not Available'}<br>
                    Seating: ${review.seating == 'PLENTY' ? 'Plenty - Always Available' :
                            review.seating == 'MODERATE' ? 'Moderate - Usually Available' :
                            review.seating == 'LIMITED' ? 'Limited - Often Full' :
                            review.seating == 'MINIMAL' ? 'Minimal - Very Few Seats' :
                            'No Seating Available'}
                </div>
                <hr>
                <div class="cafe-review">${review.reviewText}</div>
                <small>By: ${review.username}</small>
                <button onclick="prepareReview('${review.cafeName}', ${review.latitude}, ${review.longitude})"
                        class="add-review-btn">
                    Add Your Review
                </button>
            </div>
        `)
        .addTo(map);
    </c:forEach>

    // form validation
    document.querySelector('form').addEventListener('submit', function(e) {
        if (!document.getElementById('latitude').value ||
            !document.getElementById('longitude').value) {
            e.preventDefault();
            alert('Please select a location on the map');
        }
    });

    function prepareReview(cafeName, lat, lng) {
        // flll form with existing cafe details
        document.getElementById('cafeName').value = cafeName;
        document.getElementById('cafeName').readOnly = true; // Lock the cafe name
        document.getElementById('latitude').value = lat;
        document.getElementById('longitude').value = lng;
        document.getElementById('selectedLocation').textContent =
            `Selected: ${cafeName} (${lat.toFixed(6)}, ${lng.toFixed(6)})`;

        document.querySelector('.review-form').scrollIntoView({
            behavior: 'smooth'
        });
    }

    function resetReviewForm() {
        // reset form
        document.querySelector('form').reset();

        document.getElementById('cafeName').readOnly = false;

        // clear location
        document.getElementById('latitude').value = '';
        document.getElementById('longitude').value = '';
        document.getElementById('selectedLocation').textContent = 'No location selected';

        // remove current marker
        if (currentMarker) {
            map.removeLayer(currentMarker);
            currentMarker = null;
        }
    }
</script>
</body>
</html>