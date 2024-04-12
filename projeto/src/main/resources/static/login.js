document.addEventListener('DOMContentLoaded', function() {
	const loginForm = document.getElementById("loginForm");

	loginForm.addEventListener('submit', async function(event) {
		event.preventDefault();
		const username = document.getElementById("username").value;
		const password = document.getElementById("password").value;
		try {
			const response = await fetch(`http://localhost:8002/usuarios/check-usuario?username=${username}&senha=${password}`, {
				method: 'GET',
				headers: {
					'Accept': 'application/json',
					'Content-Type': 'application/json'
				}
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