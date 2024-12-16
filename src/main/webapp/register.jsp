<%--
  Created by IntelliJ IDEA.
  User: luka
  Date: 12/9/24
  Time: 1:39 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <title>Register - Cafe Reviews</title>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container">
  <h1>Register</h1>
  <% if (request.getAttribute("error") != null) { %>
  <div class="error">${error}</div>
  <% } %>
  <form action="register" method="post">
    <div class="form-group">
      <label for="username">Username:</label>
      <input type="text" id="username" name="username" required>
    </div>
    <div class="form-group">
      <label for="password">Password:</label>
      <input type="password" id="password" name="password" required>
    </div>
    <button type="submit">Register</button>
  </form>
  <p>Already have an account? <a href="login.jsp">Login</a></p>
</div>
<script>
  // Get form elements
  const form = document.querySelector('form');
  const latitudeInput = document.getElementById('latitude');
  const longitudeInput = document.getElementById('longitude');
  const selectedLocationSpan = document.getElementById('selectedLocation');
  const clearLocationBtn = document.getElementById('clearLocation');
  let currentMarker = null;

  // Initialize the map (your existing map initialization code)
  const map = L.map('map').setView([40.7128, -74.0060], 13);
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
  }).addTo(map);

  // Add click handler to map
  map.on('click', function(e) {
    // Remove existing marker if any
    if (currentMarker) {
      map.removeLayer(currentMarker);
    }

    // Add new marker
    currentMarker = L.marker(e.latlng).addTo(map);

    // Update form values
    latitudeInput.value = e.latlng.lat;
    longitudeInput.value = e.latlng.lng;
    selectedLocationSpan.textContent = `Selected: (${e.latlng.lat.toFixed(6)}, ${e.latlng.lng.toFixed(6)})`;

    // Try to get address using reverse geocoding
    fetch(`https://nominatim.openstreetmap.org/reverse?format=json&lat=${e.latlng.lat}&lon=${e.latlng.lng}`)
            .then(response => response.json())
            .then(data => {
              if (data.display_name) {
                selectedLocationSpan.textContent = data.display_name;
              }
            })
            .catch(error => console.error('Error:', error));
  });

  // Clear location button handler
  clearLocationBtn.addEventListener('click', function() {
    if (currentMarker) {
      map.removeLayer(currentMarker);
      currentMarker = null;
    }
    latitudeInput.value = '';
    longitudeInput.value = '';
    selectedLocationSpan.textContent = 'No location selected';
  });

  // Form validation
  form.addEventListener('submit', function(e) {
    if (!latitudeInput.value || !longitudeInput.value) {
      e.preventDefault();
      alert('Please select a location on the map before submitting the review.');
    }
  });

  // Add markers for existing reviews
  const reviews = [
    <c:forEach items="${reviews}" var="review">
    {
      lat: ${review.latitude},
      lng: ${review.longitude},
      name: "${review.cafeName}",
      rating: ${review.rating},
      text: "${review.reviewText}",
      username: "${review.username}"
    },
    </c:forEach>
  ];

  // Add markers for existing reviews
  reviews.forEach(review => {
    const marker = L.marker([review.lat, review.lng]).addTo(map);
    marker.bindPopup(`
            <strong>${review.name}</strong><br>
            Rating: ${review.rating}/5<br>
            Review: ${review.text}<br>
            By: ${review.username}
        `);
  });
</script>
</body>
</html>