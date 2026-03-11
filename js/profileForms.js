document.addEventListener('DOMContentLoaded', function () {

    /* Declare Variable */
    const btnEditProfile = document.getElementById('editProfileBtn');
    const usernameElement = document.getElementById('username');
    const emailElement = document.getElementById('email');
    const yearBirthElement = document.getElementById('yearBirth');
    const btnSave = document.createElement('button');
    btnSave.textContent = 'Guardar Cambios';
    btnSave.className='btn btn-primary';
    btnSave.style.display = 'none';
    btnEditProfile.parentNode.insertBefore(btnSave, btnEditProfile.nextSibling);

    /* Inserted the Html and converted to form */
    btnEditProfile.addEventListener('click', function () {

        const parts= yearBirthElement.textContent.trim().split('-');
        const formatedDate = `${parts[2]}-${parts[1]}-${parts[0]}`;

        usernameElement.innerHTML = `<input type="text" id="usernameInput" class="input-perfil-personalizado" value="${usernameElement.textContent.trim()}">`;
        emailElement.innerHTML = `<input type="email" id="emailInput" class="input-perfil-personalizado" value="${emailElement.textContent.trim()}">`;
        yearBirthElement.innerHTML = `<input type="date" id="yearBirthInput" class="input-perfil-personalizado" value="${formatedDate}">`;

        btnEditProfile.style.display = 'none';
        btnSave.style.display = 'inline-block';

    });

    /* Save Elements */
    btnSave.addEventListener('click', function () {
        const newUsername = document.getElementById('usernameInput').value;
        const newEmail = document.getElementById('emailInput').value;
        const newYearBirth = document.getElementById('yearBirthInput').value;

        const partsFinal= newYearBirth.trim().split('-');
        const formatedDateFinal = `${partsFinal[2]}-${partsFinal[1]}-${partsFinal[0]}`;

        usernameElement.textContent = newUsername;
        emailElement.textContent = newEmail;
        yearBirthElement.textContent = formatedDateFinal;


        btnEditProfile.style.display = 'inline-block';
        btnSave.style.display = 'none';
    })
});             