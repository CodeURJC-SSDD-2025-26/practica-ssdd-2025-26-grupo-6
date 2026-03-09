
document.addEventListener('DOMContentLoaded', function () {

    const ctx = document.getElementById('myPie');
    if (ctx) {
        const data = {
            labels: ['Terrror', 'Romance', 'Aventuras', 'Ciencia ficcón'],
            datasets: [{
                label: 'Mi dataset',
                data: [10, 20, 30, 25],
                backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#589245']
            }]
        };

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
        console.error("No se encontró el elemento con id ''")
    }
})
