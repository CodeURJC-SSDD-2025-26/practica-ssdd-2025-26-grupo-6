document.querySelectorAll('.star').forEach(star => {
    
    star.addEventListener('mousemove', function(e) {
        const rect = this.getBoundingClientRect();
        const half = rect.width / 2;
        const value = e.clientX - rect.left < half
            ? parseFloat(this.dataset.value) - 0.5
            : parseFloat(this.dataset.value);
        
        updateStars(value);
    });

    star.addEventListener('click', function(e) {
        const rect = this.getBoundingClientRect();
        const half = rect.width / 2;
        const selectedValue = e.clientX - rect.left < half
            ? parseFloat(this.dataset.value) - 0.5
            : parseFloat(this.dataset.value);
        
        document.getElementById('rating').value = selectedValue;
        console.log("Selected rating:", selectedValue);
    });
});

document.getElementById('stars').addEventListener('mouseleave', function() {
    const savedValue = document.getElementById('rating').value;
    updateStars(savedValue ? parseFloat(savedValue) : 0);
});

function updateStars(value) {
    document.querySelectorAll('.star').forEach(star => {
        const v = parseFloat(star.dataset.value);
        if (v <= value) {
            star.classList.remove('bi-star', 'bi-star-half');
            star.classList.add('bi-star-fill', 'active');
        } else if (v - 0.5 === value) {
            star.classList.remove('bi-star', 'bi-star-fill');
            star.classList.add('bi-star-half', 'active');
        } else {
            star.classList.remove('bi-star-fill', 'bi-star-half', 'active');
            star.classList.add('bi-star');
        }
    });
}