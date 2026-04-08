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

    const profileForm = document.createElement('form');
    profileForm.id='profileForm';
    profileForm.action='/profile/edit';
    profileForm.method= 'post';

    const inputName = document.createElement('input');
    inputName.type = 'hidden';
    inputName.name = 'accountName';

    const inputEmail = document.createElement('input');
    inputEmail.type = 'hidden';
    inputEmail.name = 'accountEmail';

    const inputBirth = document.createElement('input');
    inputBirth.type = 'hidden';
    inputBirth.name = 'accountBirthDate';

    profileForm.appendChild(inputName);
    profileForm.appendChild(inputEmail);
    profileForm.appendChild(inputBirth);
    document.body.appendChild(profileForm);



    /* Inserted the Html and converted to form */
    btnEditProfile.addEventListener('click', function () {

        /*
        const parts= yearBirthElement.textContent.trim().split('-');
        const formatedDate = `${parts[2]}-${parts[1]}-${parts[0]}`;
*/
        usernameElement.innerHTML = `<input type="text" id="usernameInput" class="custom-profile-input" value="${usernameElement.textContent.trim()}">`;
        emailElement.innerHTML = `<input type="email" id="emailInput" class="custom-profile-input" value="${emailElement.textContent.trim()}">`;
        yearBirthElement.innerHTML = `<input type="date" id="yearBirthInput" class="custom-profile-input" value="${yearBirthElement.textContent.trim()}">`;

        btnEditProfile.style.display = 'none';
        btnSave.style.display = 'inline-block';

    });

    /* Save Elements */
    btnSave.addEventListener('click', function () {
        const newUsername = document.getElementById('usernameInput').value;
        const newEmail = document.getElementById('emailInput').value;
        const newYearBirth = document.getElementById('yearBirthInput').value;
        /*
        const partsFinal= newYearBirth.trim().split('-');
        const formatedDateFinal = `${partsFinal[2]}-${partsFinal[1]}-${partsFinal[0]}`;
*/
        inputName.value = newUsername;
        inputEmail.value = newEmail;
        inputBirth.value = newYearBirth;

/*        
        btnEditProfile.style.display = 'inline-block';
        btnSave.style.display = 'none';*/
        profileForm.submit();
    })
});             