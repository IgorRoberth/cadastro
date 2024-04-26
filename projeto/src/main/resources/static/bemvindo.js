document.addEventListener('DOMContentLoaded', function() {
    const urlParams = new URLSearchParams(window.location.search);
    const nomeUsuario = urlParams.get('nome');

    if (nomeUsuario) {
        const welcomeMessage = document.getElementById("welcomeMessage");
        welcomeMessage.textContent = `Seja Bem Vindo(a), ${nomeUsuario}!`;
    }

    const btnExcluir = document.getElementById("excluirUsuario");
    btnExcluir.addEventListener("click", function() {
        if (confirm("Tem certeza de que deseja excluir sua conta? Esta ação não pode ser desfeita.")) {
            excluirUsuario(nomeUsuario); // Passando o nome do usuário para a função excluirUsuario
        }
    });

    const excluirUsuario = (nomeUsuario) => {
        fetch(`/usuarios/contadelete/${nomeUsuario}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (response.ok) {
                    alert("Conta excluída com sucesso!");
                    window.location.href = '/login.html';
                } else {
                    throw new Error('Erro ao excluir usuário');
                }
            })
            .catch(error => {
                console.error('Erro ao excluir usuário:', error);
                alert("Ocorreu um erro ao excluir o usuário. Por favor, tente novamente mais tarde.");
            });
      };
});