
document.addEventListener('DOMContentLoaded', function () {

    const ctx = document.getElementById('myReviewsBarUser');
    if (ctx) {
        const data = {
            labels: ['0', '0,5', '1', '1,5', '2', '2,5', '3', '3,5', '4', '4,5', '5' ],
            datasets: [{
                label: 'Mi dataset',
                data: [0, 1, 3, 27 , 15 , 57, 109 , 89 ,110, 60, 10],
                backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#589245']
            }]
        };

        const config = {
            type: 'bar',
            data: data,
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    title: {
                        display: true,
                        text: 'Gafico de Valoraciones de Usuario'
                    }
                }
            },
        };

        new Chart(ctx, config);
    } else {
        console.error("No se encontró el elemento con id ''")
    }
})
