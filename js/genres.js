(function () {
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }

  function init() {
    const container = document.getElementById('generosContainer');
    const addBtn = document.getElementById('addGenero');
    const removeBtn = document.getElementById('removeGenero');
    
    if (!container || !addBtn || !removeBtn) return;

    const doubleRow = document.querySelector('#generosContainer .double-row');

    addBtn.addEventListener('click', () => {
      const nuevo = crearCampoGenero();
      container.insertBefore(nuevo, doubleRow);
      nuevo.querySelector('input').focus();
    });

    removeBtn.addEventListener('click', () => {
      const campos = container.querySelectorAll('.genero-extra');
      if (campos.length > 1) {
        campos[campos.length - 1].remove();
      }
    });

  }
  
  function crearCampoGenero() {
    const wrapper = document.createElement('div');
    wrapper.className = 'genero-extra';

    wrapper.innerHTML = `
      <div class="form-floating mb-1">
          <input type="text" class="form-control" name="genres[]" placeholder="Género">
          <label class="text-dark administer-form">Género</label>
      </div>
    `;

    return wrapper;
  }
})();