
document.addEventListener('DOMContentLoaded', function () {

    const ctx = document.getElementById('myReviewsBarFilm');
    if (ctx) {

        const chartData = JSON.parse(ctx.dataset.values);
        const chartName = ctx.dataset.name;

        /* Chart Data */
        const data = {
            labels: ['0,5', '1', '1,5', '2', '2,5', '3', '3,5', '4', '4,5', '5' ],
            datasets: [{
                data: chartData,
                backgroundColor: ['#2ecc71']
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
                        text: 'Grafico de valoraciones de ' + chartName
                    }
                }
            },
        };

        new Chart(ctx, config);
    } else {
        console.error("Element with id '' not found")
    }
})
