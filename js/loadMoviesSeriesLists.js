


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