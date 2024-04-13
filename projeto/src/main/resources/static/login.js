document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById("loginForm");

    loginForm.addEventListener('submit', async function(event) {
        event.preventDefault();
        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;
        const loginData = {username: username, senha: password};

        try {
            const response = await fetch('http://localhost:8002/usuarios/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username: username, senha: password })
            });

            if (response.status === 400) {
                alert("Credenciais inválidas. Por favor, verifique seu nome de usuário e senha.");
            } else if (response.ok) {
                window.location.href = '/bemvindo.html?nome=' + username;
            } else {
                alert("Erro ao fazer login. Por favor, tente novamente mais tarde.");
            }
        } catch (error) {
            console.error('Erro durante o processo de login:', error);
            alert("Ocorreu um erro durante o login.");
        }
    });
});
