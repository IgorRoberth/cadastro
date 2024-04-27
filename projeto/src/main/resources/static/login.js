document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById("loginForm");

    loginForm.addEventListener('submit', async function(event) {
        event.preventDefault();
        const username = document.getElementById("username").value;
        const password = document.getElementById("password").value;
        
        try {
            const response = await fetch('http://localhost:8002/usuarios/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ username: username, senha: password })
            });

            const responseData = await response.json();

            if (response.status === 400) {
                if (responseData.erro === 'Mensagens de exceções.') {
                    alert('Erro: Username e senha são obrigatórios');
                } else if (responseData.erro === 'Senha incorreta ou não foi fornecida. Por favor, verifique.') {
                    alert('Erro: Senha incorreta ou não foi fornecida. Por favor, verifique.');
                } else if (responseData.erro === 'Usuário não encontrado. Por favor, cadastre-se primeiro ou verifique suas credenciais e tente novamente.') {
                    alert('Erro: Usuário não encontrado. Por favor, cadastre-se primeiro ou verifique suas credenciais e tente novamente.');
                } else {
                    alert('Erro desconhecido: ' + responseData.erro);
                }
            } else if (response.ok) {
                window.location.href = '/bemvindo.html?nome=' + username;
            } else {
                alert("Erro ao fazer login. Por favor, cadastre-se.");
            }
        } catch (error) {
            console.error('Erro durante o processo de login:', error);
            alert("Ocorreu um erro durante o login.");
        }
    });
});