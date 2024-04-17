document.addEventListener('DOMContentLoaded', function() {
	const formulario = document.querySelector("#formulario");
	const inome = document.querySelector(".nome");
	const iusername = document.querySelector(".username");
	const iemail = document.querySelector(".email");
	const isenha = document.querySelector(".senha");
	const itel = document.querySelector(".tel");

	function validarCampos() {
		if (inome.value.trim() === "") {
			alert("Por favor, preencha o campo nome.");
			return false;
		}
		if (iusername.value.trim() === "") {
			alert("Por favor, preencha o campo username.");
			return false;
		}
		if (iemail.value.trim() === "") {
			alert("Por favor, preencha o campo email.");
			return false;
		}
		if (isenha.value.trim() === "") {
			alert("Por favor, preencha o campo senha.");
			return false;
		}
		if (itel.value.trim() === "") {
			alert("Por favor, preencha o campo telefone.");
			return false;
		}
		return true;
	}

	function cadastrar() {
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
				if (!res.ok) {
					if (res.headers.get('Content-Type').includes('application/json')) {
						return res.json();
					} else {
						throw new Error('Ocorreu um erro ao processar a sua solicitação.');
					}
				}
				return res.json();
			})
			.then(function(data) {
				if (data.error) { 
					throw new Error(data.message);
				}
				console.log(data);
				alert("Usuário cadastrado com sucesso!");
				limpar();
				window.location.href = '/login.html';
			})
			.catch(function(error) {
				console.log(error);
				alert(error.message);
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
		if (validarCampos()) {
			cadastrar();
		}
	});
});