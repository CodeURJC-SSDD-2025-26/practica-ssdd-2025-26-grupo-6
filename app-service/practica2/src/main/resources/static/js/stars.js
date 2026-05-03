const stars = document.querySelectorAll('#starsContainer .star');
const ratingInput = document.getElementById('ratingInput');

function updateStarsVisual(value) {
    stars.forEach(star => {
        const starValue = parseFloat(star.getAttribute('data-value'));
        
        // Limpiamos clases de iconos de Bootstrap
        star.classList.remove('bi-star', 'bi-star-fill', 'bi-star-half', 'active', 'partial');
        
        if (starValue <= value) {
            // Estrella completa
            star.classList.add('bi-star-fill', 'active');
        } else if (starValue - 0.5 === value) {
            // Media estrella
            star.classList.add('bi-star-half', 'active');
            // Si quieres usar tu degradado CSS en lugar del icono "half":
            // star.classList.add('bi-star-fill', 'partial'); 
        } else {
            // Estrella vacía
            star.classList.add('bi-star');
        }
    });
}

stars.forEach(star => {
    star.addEventListener('click', function(e) {
        const rect = this.getBoundingClientRect();
        const x = e.clientX - rect.left; // Posición X del clic dentro de la estrella
        let val = parseFloat(this.getAttribute('data-value'));
        
        // Si el clic es en la mitad izquierda de la estrella, vale .5
        if (x < rect.width / 2) {
            val -= 0.5;
        }
        
        ratingInput.value = val;
        updateStarsVisual(val);
    });
});

// Cargar estado inicial (para edición)
if (ratingInput.value > 0) {
    updateStarsVisual(parseFloat(ratingInput.value));
}