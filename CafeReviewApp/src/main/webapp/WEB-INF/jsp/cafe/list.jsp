<%@ include file="../common/header.jsp" %>
<div class="container mt-4">
    <h2>Cafes</h2>
    <div class="row">
        <c:forEach items="${cafes}" var="cafe">
            <div class="col-md-4 mb-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">${cafe.name}</h5>
                        <p class="card-text">
                            <strong>Address:</strong> ${cafe.address}<br>
                            <strong>Phone:</strong> ${cafe.phone}<br>
                            <strong>WiFi:</strong> ${cafe.wifiAvailable ? 'Available' : 'Not Available'}
                        </p>
                        <a href="${pageContext.request.contextPath}/cafes/${cafe.cafeId}" class="btn btn-primary">View Reviews</a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
<%@ include file="../common/footer.jsp" %>
