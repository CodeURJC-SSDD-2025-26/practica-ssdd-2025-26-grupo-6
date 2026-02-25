(function () {
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }

  function init() {
    const container = document.getElementById('camposPlataformas');
    const addBtn = document.getElementById('addPlatform');
    const removeBtn = document.getElementById('removePlatform');

    if (!container || !addBtn || !removeBtn) return;

    addBtn.addEventListener('click', () => {
      const nuevo = crearCampoPlataforma();
      container.appendChild(nuevo);
      nuevo.querySelector('input').focus();
    });

    removeBtn.addEventListener('click', () => {
      const campos = container.querySelectorAll('.platform-extra');
      if (campos.length > 1) {
        campos[campos.length - 1].remove();
      }
    });
  }

  function crearCampoPlataforma() {
    const wrapper = document.createElement('div');
    wrapper.className = 'input-group platform-extra';
    wrapper.innerHTML = `
      <input type="text" class="form-control" name="platforms[]" placeholder="Plataforma">
    `;
    return wrapper;
  }
})();