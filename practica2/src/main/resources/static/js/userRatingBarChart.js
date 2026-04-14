
document.addEventListener('DOMContentLoaded', function () {

    const ctx = document.getElementById('myReviewsBarUser');
    if (ctx) {

        const chartData= JSON.parse(ctx.dataset.values);
        const username = ctx.dataset.username;

        /* Chart Data */
        const data = {
            labels: ['0,5', '1', '1,5', '2', '2,5', '3', '3,5', '4', '4,5', '5' ],
            datasets: [{
                data: chartData,
                backgroundColor: ['#2ecc71']
            }]
        };

        /*Chart Shape*/
        const config = {
            type: 'bar',
            data: data,
            options: {
                responsive: true,
                maintainAspectRatio: true,
                plugins: {
                    legend: {
                        position: 'top',
                        display: false
                    },
                    title: {
                        display: true,
                        text: `Grafico de Valoraciones de ${username}`,
                        font: {
                            size: 20,
                            weight: 'bold'
                        },
                        color: '#bbbbbb'

                    }
                }
            },
        };

        new Chart(ctx, config);
    } else {
        console.error("Element with id '' not found")
    }
})
