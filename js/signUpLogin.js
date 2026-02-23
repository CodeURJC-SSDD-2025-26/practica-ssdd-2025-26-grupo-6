
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
        alert('Correo electrónico no valido');
    } else {  //redirect to principal.html
        document.location.href = 'principal.html';
    }

    //check on bbdd 
}

//SIGN UP FUNCTIONALITY
document.getElementById('signUpBtn')?.addEventListener('click', signUp);

function signUp() {

    let username = document.getElementById('floatingInput').value;
    let password = document.getElementById('floatingPassword').value;
    let repeatPassword = document.getElementById('floatingPassword2').value;
    let birthYear = document.getElementById('floatingBirthYear').value;





}