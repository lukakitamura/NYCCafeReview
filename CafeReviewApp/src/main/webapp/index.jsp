<%@ include file="/WEB-INF/jsp/common/header.jsp" %>
<div class="container mt-4">
    <div class="jumbotron text-center">
        <h1 class="display-4">Welcome to Cafe Reviews</h1>
        <p class="lead">Discover and review local cafes in your area.</p>
        <hr class="my-4">
        <p>Browse reviews, share your experiences, and find your next favorite spot.</p>
        <a class="btn btn-primary btn-lg" href="${pageContext.request.contextPath}/cafes" role="button">Browse Cafes</a>
    </div>
</div>
<%@ include file="/WEB-INF/jsp/common/footer.jsp" %>
