(function () {
  //Check if the DOM is loaded
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
  } else {
    init();
  }

  function init() {
    const container = document.getElementById('platformField');
    const addBtn = document.getElementById('addPlatform');
    const removeBtn = document.getElementById('removePlatform');

    //Safety check, ensure all the required elements are in the DOM
    if (!container || !addBtn || !removeBtn) return;

    //Add a new genre field
    addBtn.addEventListener('click', () => {
      const newF = createPlatformField();
      container.appendChild(newF);
      nuevo.querySelector('input').focus();
    });

    //Remove the last genre field
    removeBtn.addEventListener('click', () => {
      const field = container.querySelectorAll('.platform-extra');
      if (field.length > 1) {
        field[field.length - 1].remove();
      }
    });
  }

  //Create and return the input group
  function createPlatformField() {
    const wrapper = document.createElement('div');
    wrapper.className = 'input-group platform-extra';
    wrapper.innerHTML = `
      <input type="text" class="form-control" name="platforms[]" placeholder="Plataforma">
    `;
    return wrapper;
  }
})();