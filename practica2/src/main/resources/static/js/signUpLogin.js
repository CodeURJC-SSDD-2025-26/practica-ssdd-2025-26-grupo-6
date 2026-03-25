
//LOGIN FUNCTIONALITY
document.getElementById('loginButton')?.addEventListener('click', login);
document.getElementById('resetPass')?.addEventListener('click', resetPassword);

function resetPassword() {
    const myModal = document.getElementById('myModal')
    const myInput = document.getElementById('myInput')

    myModal.addEventListener('shown.bs.modal', () => {
        myInput.focus()
    })
}

function login() {

    let email = document.getElementById('email').value;
    let password = document.getElementById('password').value;

    let expRegEmail = /[\w-.]+@[\w-]+.[a-z]/;
    //check correctness of username and password
    if (!(email && password)) {
        alert('Correo electrónico y/o contraseña vacío');
    } else if (!expRegEmail.test(email)) {
        alert('Correo electrónico no valido. Ejemplo: name@example.com');
    } else {  //redirect to principal.html
        document.location.href = 'principal.html';
    }

    //check on bbdd 
}

//SIGN UP FUNCTIONALITY
document.getElementById('signUpBtn')?.addEventListener('click', signUp);

function signUp() {

    let username = document.getElementById('user').value;
    let password = document.getElementById('password').value;
    let repeatPassword = document.getElementById('password2').value;
    let birthDate = document.getElementById('birthDate').value;

    //check if user exists in bbdd

    //check if passwords match
    if (password !== repeatPassword) {
        alert('Las contraseñas no coinciden');
    }
    //check if all fields are filled
    if (!username || !password || !repeatPassword || !birthDate) {
        alert('Rellena todos los campos');
    }
    //check if birth date is valid
    let today = Date.now();
    let birthDateTime = new Date(birthDate).getTime();
    if (birthDateTime >= today) {
        alert('Fecha de nacimiento no válida');
    }

    //add user to bbdd
    document.location.href = 'login.html';
}

document.getElementById('cancelBtn')?.addEventListener('click', cancelOp);

function cancelOp() {
    document.location.href = 'login.html';
}
