(function () {
  //Check if the DOM is loaded
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }

  function init() {
    const container = document.getElementById('genreFields');
    const addBtn = document.getElementById('addGenre');
    const removeBtn = document.getElementById('removeGenre');

    //Safety check, ensure all the required elements are in the DOM
    if (!container || !addBtn || !removeBtn) return;    

    //Add a new genre field
    addBtn.addEventListener('click', () => {
      const newF = createGenreField();
      container.appendChild(newF);
      nuevo.querySelector('input').focus();
    });

    //Remove the last genre field
    removeBtn.addEventListener('click', () => {
      const fields = container.querySelectorAll('.genre-extra');
      if (fields.length > 1) {
        fields[fields.length - 1].remove();
      }
    });
  }

  //Create and return the input group
  function createGenreField() {
    const wrapper = document.createElement('div');
    wrapper.className = 'input-group genre-extra';
    wrapper.innerHTML = `
      <input type="text" class="form-control" name="genres[]" placeholder="Género">
    `;
    return wrapper;
  }
})();