(() => {
    console.log("ENTRA AL SCRIPT");
    function verContrasena() {

    const btnVerContrasena = document.getElementById('btnVerContrasena');
    const icono = document.getElementById('verContrasena');
    const password = document.getElementById('password');

    btnVerContrasena.addEventListener('click', () => {
        
        if (password.type === 'password') {
            password.type = 'text';

            icono.classList.remove('bi-eye-slash-fill');
            icono.classList.add('bi-eye-fill');
        } else {
            password.type = 'password';

            icono.classList.remove('bi-eye-fill');
            icono.classList.add('bi-eye-slash-fill');
        }
    });
    }

    verContrasena();

})();