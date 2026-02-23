//EXAMPLES

const peliculas = [
    { titulo: 'Avatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'Avatar2', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'Avatar3', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'Avatar4', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'Avatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'Avatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'Avatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'Avatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'Avatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'Avatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' }
];

const series = [
    { titulo: 'SAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'SAvatar2', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'SAvatar3', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'SAvatar4', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'SAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'SAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'SAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'SAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'SAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'SAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' }
];

const listas = [
    { titulo: 'LAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'LAvatar2', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'LAvatar3', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'LAvatar4', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'LAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'LAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'LAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'LAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'LAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' },
    { titulo: 'LAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'movieDetails.html' }
];


function cargarContenido(datos) {
    const containers = document.querySelectorAll('.films');
    let htmlContent = '';

    datos.forEach(item => {
        htmlContent += `
            <div class="card">
                <a href="${item.link}">
                    <img src="${item.imagen}" alt="${item.titulo}">
                    <h5>${item.titulo}</h5>
                </a> 
            </div>`;
    });

    containers.forEach(div => div.innerHTML = htmlContent);
}

//depends on the page, load the content of peliculas, series or listas
document.addEventListener('DOMContentLoaded', () => {

    if (document.getElementById('page-peliculas')) {
        cargarContenido(peliculas);
    }

    else if (document.getElementById('page-series')) {
        console.log("Cargando Series...");
        cargarContenido(series);
    }

    else if (document.getElementById('page-listas')) {
        console.log("Cargando Listas...");
        cargarContenido(listas);
    }
});