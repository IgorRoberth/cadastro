document.addEventListener('DOMContentLoaded', function () {
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