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
    { titulo: 'SAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'seriesDetails.html' },
    { titulo: 'SAvatar2', imagen: 'images/carteleraEjemploAvatar.webp', link: 'seriesDetails.html' },
    { titulo: 'SAvatar3', imagen: 'images/carteleraEjemploAvatar.webp', link: 'seriesDetails.html' },
    { titulo: 'SAvatar4', imagen: 'images/carteleraEjemploAvatar.webp', link: 'seriesDetails.html' },
    { titulo: 'SAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'seriesDetails.html' },
    { titulo: 'SAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'seriesDetails.html' },
    { titulo: 'SAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'seriesDetails.html' },
    { titulo: 'SAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'seriesDetails.html' },
    { titulo: 'SAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'seriesDetails.html' },
    { titulo: 'SAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'seriesDetails.html' }
];

const listas = [
    { titulo: 'LAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'filmsLists.html' },
    { titulo: 'LAvatar2', imagen: 'images/carteleraEjemploAvatar.webp', link: 'filmsLists.html' },
    { titulo: 'LAvatar3', imagen: 'images/carteleraEjemploAvatar.webp', link: 'filmsLists.html' },
    { titulo: 'LAvatar4', imagen: 'images/carteleraEjemploAvatar.webp', link: 'filmsLists.html' },
    { titulo: 'LAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'filmsLists.html' },
    { titulo: 'LAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'filmsLists.html' },
    { titulo: 'LAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'filmsLists.html' },
    { titulo: 'LAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'filmsLists.html' },
    { titulo: 'LAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'filmsLists.html' },
    { titulo: 'LAvatar', imagen: 'images/carteleraEjemploAvatar.webp', link: 'filmsLists.html' }
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
            <div class=mb-5><h2>Resultado de búsqueda para: ${query}</h2></div>`;

        if (filteredPeliculas.length === 0 && filteredSeries.length === 0 && filteredListas.length === 0) {
            html.innerHTML += `
                <div class="container d-flex justify-content-center align-items-center text-center">
                    <div class="row align-items-center justify-content-center">
                        
                        <div class="col-8 col-sm-6 col-md-4 col-lg-3">
                            <img class="img-fluid" src="images/searchError.png" alt="Imagen Error">
                        </div>

                        <div class="col-12 col-sm-auto text-center text-md-start mt-3 mt-sm-0">
                            <h2 class="mb-2">No se encuentran resultados para tu búsqueda</h2>
                            <p class="fs-5">Prueba con otra palabra clave</p>
                        </div>

                    </div>
                </div>`;
            return;
        }

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