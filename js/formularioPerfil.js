document.addEventListener('DOMContentLoaded', function () {
    const btnEditarPerfil = document.getElementById('editarPerfilBtn');
    const usernameElement = document.getElementById('username');
    const emailElement = document.getElementById('email');
    const yearBirthElement = document.getElementById('yearBirth');
    const btnGuardar = document.createElement('button');
    btnGuardar.textContent = 'Guardar Cambios';
    btnGuardar.className='btn btn-primary';
    btnGuardar.style.display = 'none';
    btnEditarPerfil.parentNode.insertBefore(btnGuardar, btnEditarPerfil.nextSibling);


    btnEditarPerfil.addEventListener('click', function () {

        const parts= yearBirthElement.textContent.trim().split('-');
        const formatedDate = `${parts[2]}-${parts[1]}-${parts[0]}`;

        usernameElement.innerHTML = `<input type="text" id="usernameInput" class="input-perfil-personalizado" value="${usernameElement.textContent.trim()}">`;
        emailElement.innerHTML = `<input type="email" id="emailInput" class="input-perfil-personalizado" value="${emailElement.textContent.trim()}">`;
        yearBirthElement.innerHTML = `<input type="date" id="yearBirthInput" class="input-perfil-personalizado" value="${formatedDate}">`;

        btnEditarPerfil.style.display = 'none';
        btnGuardar.style.display = 'inline-block';

    });

    btnGuardar.addEventListener('click', function () {
        const newUsername = document.getElementById('usernameInput').value;
        const newEmail = document.getElementById('emailInput').value;
        const newYearBirth = document.getElementById('yearBirthInput').value;

        const partsFinal= newYearBirth.trim().split('-');
        const formatedDateFinal = `${partsFinal[2]}-${partsFinal[1]}-${partsFinal[0]}`;

        usernameElement.textContent = newUsername;
        emailElement.textContent = newEmail;
        yearBirthElement.textContent = formatedDateFinal;


        btnEditarPerfil.style.display = 'inline-block';
        btnGuardar.style.display = 'none';
    })
});             