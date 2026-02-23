(function () {
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }

  function init() {
    const container = document.getElementById('platformsContainer');
    const addBtn = document.getElementById('addPlatform');
    const removeBtn = document.getElementById('removePlatform');

    if (!container || !addBtn || !removeBtn) return;

   const doubleRow = document.querySelector('#platformsContainer .double-row');

    addBtn.addEventListener('click', () => {
      const nuevo = crearCampoPlataforma();
      container.insertBefore(nuevo, doubleRow);
      nuevo.querySelector('input').focus();
    });

    removeBtn.addEventListener('click', () => {
      const campos = container.querySelectorAll('.platform-extra');
      if (campos.length > 1) {
        container.removeChild(campos[campos.length - 1]);
      }
    });

  }
  
  function crearCampoPlataforma() {
    const wrapper = document.createElement('div');
    wrapper.className = 'platform-extra';

    wrapper.innerHTML = `
      <div class="form-floating mb-1">
          <input type="text" class="form-control" name="platforms[]" placeholder="Plataformas disponibles">
          <label class="text-dark administer-form">Plataformas disponibles</label>
      </div>
    `;

    return wrapper;
  }
})();