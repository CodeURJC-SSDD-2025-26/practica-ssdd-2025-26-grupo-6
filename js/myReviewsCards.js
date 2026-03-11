const reviews = [
    { 
        id: 1, 
        username: 'CarlaGarcíaRomero', 
        rating: 4, 
        text: 'Esta película me ha sorprendido muchísimo. La historia está muy bien construida, los personajes tienen profundidad y el ritmo no decae en ningún momento. La fotografía es espectacular y la banda sonora acompaña perfectamente cada escena. James Cameron ha conseguido crear un mundo completamente creíble, con una cultura, una fauna y una flora únicas. La relación entre Jake y Neytiri es emotiva y está muy bien desarrollada a lo largo del metraje. Sin duda una de las mejores películas de ciencia ficción que he visto en años.'
    },
    { 
        id: 2, 
        username: 'PepitoGrillo99', 
        rating: 5, 
        text: 'Una obra maestra del cine moderno que redefine los límites de lo que es posible hacer en pantalla. Los efectos visuales son simplemente increíbles, nunca había visto nada igual. Pandora es un mundo que te atrapa desde el primer momento y no te suelta hasta los créditos finales. La historia, aunque sencilla en su estructura, tiene una profundidad emocional que te llega al alma. La actuación de Sam Worthington es magnífica y Zoe Saldana da vida a un personaje que recordarás durante mucho tiempo. Absolutamente imprescindible.'
    },
    { 
        id: 3, 
        username: 'MariaLopez22', 
        rating: 3, 
        text: 'Buena película pero algo larga. Los efectos especiales son increíbles y la ambientación de Pandora es verdaderamente espectacular, pero la historia se hace repetitiva en algunos momentos y se nota que el guion no está a la altura de la producción visual. Los personajes secundarios están algo descuidados y hay momentos en los que el ritmo decae bastante. Aun así, es una experiencia cinematográfica que merece la pena vivir en el cine por la espectacularidad de sus imágenes y su banda sonora envolvente.'
    },
    { 
        id: 4, 
        username: 'JuanMartinez', 
        rating: 4, 
        text: 'Me gustó mucho, especialmente la segunda mitad. Los personajes están muy bien desarrollados y la ambientación es espectacular. La historia de redención del protagonista está contada con mucha habilidad y el conflicto entre los humanos y los Na\'vi tiene una carga política y ecológica muy interesante que hace reflexionar al espectador. La batalla final es una de las secuencias de acción más impresionantes que he visto en el cine. James Cameron demuestra una vez más por qué es uno de los mejores directores de su generación.'
    },
    { 
        id: 5, 
        username: 'LauraSanchez88', 
        rating: 5, 
        text: 'Simplemente alucinante. Fui al cine sin muchas expectativas y salí completamente enamorada de esta película. La construcción del mundo de Pandora es tan detallada y meticulosa que te olvidas de que todo es CGI. Los colores, las criaturas, la vegetación luminosa... todo está diseñado con un mimo y una atención al detalle que no tiene parangón. La historia de amor entre Jake y Neytiri es preciosa y emotiva, y el mensaje ecologista está integrado de forma natural en la narrativa sin resultar panfletario. Una película que hay que ver al menos una vez en la vida.'
    },
    { 
        id: 6, 
        username: 'RobertoFernandez', 
        rating: 2, 
        text: 'No entiendo tanto entusiasmo. La historia es un calco descarado de Bailando con Lobos y Pocahontas, con los mismos tópicos de siempre sobre el hombre blanco que salva a los nativos. Los personajes son planos y predecibles, y el villano es un cliché andante sin ninguna profundidad. Es cierto que visualmente es impresionante, pero los efectos especiales no pueden compensar un guion tan pobre. Para mi gusto, Cameron debería haber dedicado tanto esfuerzo al desarrollo del guion como al apartado técnico. Una oportunidad perdida.'
    },
    { 
        id: 7, 
        username: 'AnaGomezCine', 
        rating: 4, 
        text: 'Avatar es una experiencia cinematográfica única que va mucho más allá de lo que cualquier película había conseguido hasta entonces en términos de efectos visuales. La inmersión en el mundo de Pandora es total y la dirección de Cameron es brillante en cada secuencia. Lo que más me sorprendió fue la coherencia interna del mundo que ha creado: tiene su propia biología, su propio idioma y su propia cosmología. La música de James Horner complementa perfectamente las imágenes y contribuye enormemente a crear esa atmósfera tan especial. Una película que merece todos los récords que ha batido.'
    },
    { 
        id: 8, 
        username: 'DiegoRuizFilms', 
        rating: 3, 
        text: 'Avatar es técnicamente impecable y visualmente deslumbrante, no hay duda de ello. Sin embargo, creo que su legado cinematográfico ha sido sobrevalorado. La historia no aporta nada nuevo al género y los personajes, aunque correctos, no llegan a conectar emocionalmente con el espectador de la misma manera que otros grandes títulos de ciencia ficción. Aun así, hay que reconocer el mérito de Cameron por haber empujado los límites de la tecnología cinematográfica y haber creado un espectáculo visual sin precedentes. Vale la pena verla, pero sin esperar una revolución narrativa.'
    }
];

function createCard(r) {
    return `
        <div class="card h-100">
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-center">
                    <h5 class="card-title m-0">${r.username}</h5>
                    <div class="rating d-flex align-items-center">
                        <h4 class="punctuation m-0 me-1">${r.rating}</h4>
                        <i class="bi bi-star-fill fs-4 text-warning"></i>
                    </div>
                </div>
                <p class="card-text mt-3" style="text-align: justify;">${r.text}</p>
                <hr class="m-0">
                <div class="d-flex justify-content-between">
                    <div class="card-footer bg-transparent py-1 border-0 d-flex gap-2">
                        <a href="modifyReview.html?id=${r.id}" class="btn btn-outline-primary btn-sm">
                            <i class="bi bi-pencil"></i>
                        </a>
                        <button class="btn btn-outline-danger btn-sm">
                            <i class="bi bi-trash"></i>
                        </button>
                    </div>
                    <div class="card-footer bg-transparent py-1 border-0">
                        <a class="btn btn-primary btn-sm btn-small" data-bs-toggle="modal" data-bs-target="#modal${r.id}">Ver más</a>
                    </div>
                </div>
            </div>
        </div>`;
        
}

function createModal(r) {
    return `
        <div class="modal fade" id="modal${r.id}" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered modal-lg">
                <div class="modal-content">
                    <div class="card">
                        <div class="card-body">
                            <div class="d-flex justify-content-between align-items-center">
                                <h5 class="card-title m-0">${r.username}</h5>
                                <div class="rating d-flex align-items-center">
                                    <h4 class="punctuation m-0 me-1">${r.rating}</h4>
                                    <i class="bi bi-star-fill fs-4 text-warning"></i>
                                </div>
                            </div>
                            <p class="card-text mt-3" style="text-align: justify;">${r.text}</p>
                            <hr class="m-2">
                            <div class="d-flex justify-content-end">
                                <button class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>`;
}

function loadReviews(data) {
    const container = document.getElementById('reviews-container');
    if (!container) return;
    container.innerHTML = data.map(r => `<div class="col">${createCard(r)}${createModal(r)}</div>`).join('');
}

document.addEventListener('DOMContentLoaded', () => {
    if (document.getElementById('page-reviews')) loadReviews(reviews);
});