document.addEventListener('DOMContentLoaded', function() {
	const loginForm = document.getElementById("loginForm");
	const forgotPasswordLink = document.getElementById("forgot-password-link");

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

			if (response.ok) {
				window.location.href = '/cadastrarprod.html?nome=' + username;
			} else {
				const responseData = await response.json();
				const errorMessage = responseData.erro || 'Erro desconhecido';

				if (response.status === 400) {
					if (errorMessage.includes('Mensagens de exceções.')) {
						alert('Erro: Username e senha são obrigatórios');
					} else if (errorMessage.includes('Senha incorreta ou não foi fornecida.')) {
						alert('Erro: Senha incorreta ou não foi fornecida. Por favor, verifique.');
					} else if (errorMessage.includes('Usuário não encontrado.')) {
						alert('Erro: Usuário não encontrado. Por favor, cadastre-se primeiro ou verifique suas credenciais e tente novamente.');
					} else {
						alert('Erro desconhecido: ' + errorMessage);
					}
				} else {
					alert("Erro ao fazer login. Por favor, cadastre-se.");
				}
			}
		} catch (error) {
			console.error('Erro durante o processo de login:', error);
			alert("Ocorreu um erro durante o login.");
		}
	});
});
