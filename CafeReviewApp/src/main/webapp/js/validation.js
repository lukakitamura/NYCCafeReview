// Form validation
document.addEventListener('DOMContentLoaded', function() {
    // Review form validation
    const reviewForm = document.querySelector('form[action*="/review"]');
    if (reviewForm) {
        reviewForm.addEventListener('submit', function(e) {
            const rating = document.getElementById('rating').value;
            const comment = document.getElementById('comment').value;
            
            if (!rating) {
                e.preventDefault();
                alert('Please select a rating');
                return;
            }
            
            if (comment.length < 10) {
                e.preventDefault();
                alert('Comment must be at least 10 characters long');
                return;
            }
        });
    }

    // Registration form validation
    const registerForm = document.querySelector('form[action*="/register"]');
    if (registerForm) {
        registerForm.addEventListener('submit', function(e) {
            const username = document.getElementById('username').value;
            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;
            
            if (username.length < 3) {
                e.preventDefault();
                alert('Username must be at least 3 characters long');
                return;
            }
            
            if (!isValidEmail(email)) {
                e.preventDefault();
                alert('Please enter a valid email address');
                return;
            }
            
            if (password.length < 6) {
                e.preventDefault();
                alert('Password must be at least 6 characters long');
                return;
            }
        });
    }
});

// Helper functions
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

// Star rating display
function updateStarRating(value) {
    const stars = document.querySelectorAll('.star-rating span');
    stars.forEach((star, index) => {
        star.classList.toggle('active', index < value);
    });
}
