document.addEventListener('DOMContentLoaded', function() {
	const formulario = document.querySelector("#formulario");
	const inome = document.querySelector(".nome");
	const iusername = document.querySelector(".username");
	const iemail = document.querySelector(".email");
	const isenha = document.querySelector(".senha");
	const itel = document.querySelector(".tel");

	function validarCampos() {
		return (
			inome.value.trim() !== "" &&
			iusername.value.trim() !== "" &&
			iemail.value.trim() !== "" &&
			isenha.value.trim() !== "" &&
			itel.value.trim() !== ""
		);
	}

	function cadastrar() {
		if (!validarCampos()) {
			alert("Por favor, preencha todos os campos.");
			return;
		}
		
		fetch("http://localhost:8002/usuarios", {
			headers: {
				'Accept': 'application/json',
				'Content-Type': 'application/json'
			},
			method: "POST",
			body: JSON.stringify({
				nome: inome.value,
				username: iusername.value,
				email: iemail.value,
				senha: isenha.value,
				telefone: itel.value
			})
		})
			.then(function(res) {
				if (res.ok) {
					return res.json();
				} else if (res.status === 400 || res.status === 409) {
					return res.json().then(function(errorData) {
						throw new Error(errorData.error || "Erro desconhecido ao cadastrar o usuário.");
					});
				} else {
					throw new Error("Ocorreu um erro ao cadastrar o usuário.");
				}
			})
			.then(function(data) {
				alert("Usuário cadastrado com sucesso!");
				limpar();
				window.location.href = '/login.html';
			})
			.catch(function(error) {
				console.error(error);
				alert("Erro ao cadastrar o usuário: " + error.message);
			});
	}

	function limpar() {
		inome.value = "";
		iusername.value = "";
		iemail.value = "";
		isenha.value = "";
		itel.value = "";
	}

	formulario.addEventListener('submit', function(event) {
		event.preventDefault();
		cadastrar();
	});
});