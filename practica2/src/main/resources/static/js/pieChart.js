
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
                backgroundColor: ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de', 
    '#3ba272', '#fc8452', '#9a60b4', '#ea7ccc', '#2f4554']
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
