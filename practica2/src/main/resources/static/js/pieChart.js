
document.addEventListener('DOMContentLoaded', function () {

    const ctx = document.getElementById('myPie');
    if (ctx) {

        const chartLabels = JSON.parse(ctx.dataset.labels);
        const chartData = JSON.parse(ctx.dataset.values);

        /* Chart Data */
        const data = {
            labels: chartLabels,
            datasets: [{
                data: chartData,
                backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#589245']
            }]
        };

        /* Chart Shape */
        const config = {
            type: 'pie',
            data: data,
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    title: {
                        display: true,
                        text: 'Grafico de Géneros'
                    }
                }
            },
        };

        new Chart(ctx, config);
    } else {
        console.error("Element with id '' not found")
    }
})
