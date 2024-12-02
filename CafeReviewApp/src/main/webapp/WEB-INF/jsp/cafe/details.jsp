<%@ include file="../common/header.jsp" %>
<div class="container mt-4">
    <div class="card mb-4">
        <div class="card-header">
            <h2>${cafe.name}</h2>
        </div>
        <div class="card-body">
            <p><strong>Address:</strong> ${cafe.address}</p>
            <p><strong>Phone:</strong> ${cafe.phone}</p>
            <p><strong>WiFi:</strong> ${cafe.wifiAvailable ? 'Available' : 'Not Available'}</p>
            <p><strong>Average Rating:</strong> ${averageRating}/5.0</p>
        </div>
    </div>

    <c:if test="${not empty sessionScope.user}">
        <div class="card mb-4">
            <div class="card-header">Write a Review</div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/review" method="post">
                    <input type="hidden" name="cafeId" value="${cafe.cafeId}">
                    <div class="mb-3">
                        <label for="rating" class="form-label">Rating</label>
                        <select name="rating" id="rating" class="form-select" required>
                            <option value="">Select rating</option>
                            <c:forEach begin="1" end="5" var="i">
                                <option value="${i}">${i} stars</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="comment" class="form-label">Comment</label>
                        <textarea class="form-control" id="comment" name="comment" rows="3" required></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary">Submit Review</button>
                </form>
            </div>
        </div>
    </c:if>

    <jsp:include page="../review/list.jsp" />
</div>
<%@ include file="../common/footer.jsp" %>
