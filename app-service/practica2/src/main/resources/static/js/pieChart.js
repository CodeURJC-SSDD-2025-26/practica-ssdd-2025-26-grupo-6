
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
                    '#3ba272', '#fc8452', '#9a60b4', '#ea7ccc', '#2f4554',
                    '#5b8ff9', '#61ddaa', '#65789b', '#f6bd16', '#7262fd',
                    '#78d3f8', '#9661bc', '#f6903d', '#008685', '#f08bb4',
                    '#dcb43c', '#945fb9', '#1e9493']
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
