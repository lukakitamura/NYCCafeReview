<h3>Reviews</h3>
<div class="reviews">
    <c:forEach items="${reviews}" var="review">
        <div class="card mb-3">
            <div class="card-body">
                <div class="d-flex justify-content-between">
                    <h6 class="card-subtitle mb-2 text-muted">By ${review.user.username}</h6>
                    <span class="text-warning">★ ${review.rating}/5</span>
                </div>
                <p class="card-text">${review.comment}</p>
                <small class="text-muted">Posted on ${review.createdAt}</small>
            </div>
        </div>
    </c:forEach>
</div>
