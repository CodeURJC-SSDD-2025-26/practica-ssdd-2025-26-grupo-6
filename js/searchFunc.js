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
    { titulo: 'LAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'peliculasLista.html' },
    { titulo: 'LAvatar2', imagen: 'images/carteleraEjemploAvatar.webp', link: 'peliculasLista.html' },
    { titulo: 'LAvatar3', imagen: 'images/carteleraEjemploAvatar.webp', link: 'peliculasLista.html' },
    { titulo: 'LAvatar4', imagen: 'images/carteleraEjemploAvatar.webp', link: 'peliculasLista.html' },
    { titulo: 'LAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'peliculasLista.html' },
    { titulo: 'LAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'peliculasLista.html' },
    { titulo: 'LAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'peliculasLista.html' },
    { titulo: 'LAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'peliculasLista.html' },
    { titulo: 'LAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'peliculasLista.html' },
    { titulo: 'LAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'peliculasLista.html' }
];


//SEARCH FUNCTIONALITY
document.getElementById('searchInput')?.addEventListener('keypress', function (k) { if (k.key === 'Enter') { search(); } });
document.getElementById('searchBtn')?.addEventListener('click', search);

function search() {

    let query = document.getElementById('searchInput').value.toLowerCase().trim();
    const html = document.querySelector('.content');

    if (query !== '') {
        query = query.toLowerCase();
        const filteredPeliculas = peliculas.filter(pelicula => pelicula.titulo.toLowerCase().includes(query));
        const filteredSeries = series.filter(serie => serie.titulo.toLowerCase().includes(query));
        const filteredListas = listas.filter(lista => lista.titulo.toLowerCase().includes(query));

        html.innerHTML = `
            <div class="row">
                <div class="col"><h2>Resultado de búsqueda para: ${query}</h2></div>
            </div>`;

        if (filteredPeliculas.length > 0) {
            html.innerHTML += `<h3>Películas:</h3>
                <div class="result" id="searchResultPeliculas">
                </div>`;
        }
        if (filteredSeries.length > 0) {
            html.innerHTML += `<h3>Series:</h3>
                <div class="result" id="searchResultSeries">
                </div>`;
        }
        if (filteredListas.length > 0) {
            html.innerHTML += `<h3>Listas:</h3>
                <div class="result" id="searchResultListas">
                </div>`;
        }

        let res = document.getElementById('searchResultPeliculas');
        // load the results of the search
        filteredPeliculas.forEach(pelicula => {
            res.innerHTML += `
                <div class="card">
                <a href="${pelicula.link}">
                    <img src="${pelicula.imagen}" alt="${pelicula.titulo}">
                    <h5>${pelicula.titulo}</h5>
                </a> 
            </div>`;
        });

        res = document.getElementById('searchResultSeries');
        filteredSeries.forEach(serie => {
            res.innerHTML += `
                
                <div class="card">
                <a href="${serie.link}">
                    <img src="${serie.imagen}" alt="${serie.titulo}">
                    <h5>${serie.titulo}</h5>
                </a> 
            </div>`;
        });

        res = document.getElementById('searchResultListas');

        filteredListas.forEach(lista => {
            res.innerHTML += `
                <div class="card">
                <a href="${lista.link}">
                    <img src="${lista.imagen}" alt="${lista.titulo}">
                    <h5>${lista.titulo}</h5>
                </a> 
            </div>`;
        });
    }
}