
document.addEventListener('DOMContentLoaded', function () {

    const ctx = document.getElementById('myReviewsBarFilm');
    if (ctx) {

        /* Chart Data */
        const data = {
            labels: ['0', '0,5', '1', '1,5', '2', '2,5', '3', '3,5', '4', '4,5', '5' ],
            datasets: [{
                data: [450, 837, 1212,6969, 2523, 4657,7523,14006,9305, 15685, 9565],
                backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#589245']
            }]
        };

        /* Chart Shape */ 
        const config = {
            type: 'bar',
            data: data,
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                        display: false
                    },
                    title: {
                        display: true,
                        text: 'Grafico de valoraciones de películas'
                    }
                }
            },
        };

        new Chart(ctx, config);
    } else {
        console.error("Element with id '' not found")
    }
})
