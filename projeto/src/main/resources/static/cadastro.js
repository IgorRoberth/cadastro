document.addEventListener('DOMContentLoaded', function () {
    const formulario = document.querySelector("#formulario");
    const inome = document.querySelector(".nome");
    const iusername = document.querySelector(".username");
    const iemail = document.querySelector(".email");
    const isenha = document.querySelector(".senha");
    const itel = document.querySelector(".tel");

    function validarCampos() {
        return (inome.value.trim() !== "" && iusername.value.trim() !== "" && iemail.value.trim() !== "" && isenha.value.trim() !== "" && itel.value.trim() !== "");
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
        .then(function (res) {
            console.log(res);
            if (res.status === 201) {
                alert("Usuário cadastrado com sucesso!");
                limpar();
                window.location.href = '/login.html';
            } else {
                alert("Ocorreu um erro ao cadastrar o usuário.");
            }
        })
        .catch(function(error) {
            console.log(error);
            alert("Ocorreu um erro ao cadastrar o usuário.");
        });
    }

    function limpar() {
        inome.value = "";
        iusername.value = "";
        iemail.value = "";
        isenha.value = "";
        itel.value = "";
    }

    formulario.addEventListener('submit', function (event) {
        event.preventDefault(); 
        if (!validarCampos()) {
            alert("Por favor, preencha todos os campos.");
            return;
        }
        cadastrar();
    });
});