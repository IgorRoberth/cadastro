document.addEventListener('DOMContentLoaded', function () {
    const urlParams = new URLSearchParams(window.location.search);
    const nomeUsuario = urlParams.get('nome');

    if (nomeUsuario) {
        const welcomeMessage = document.getElementById("welcomeMessage");
        welcomeMessage.textContent = `Seja Bem Vindo(a), ${nomeUsuario}!`;
    }
});