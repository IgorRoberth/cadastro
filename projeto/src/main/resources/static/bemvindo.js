document.addEventListener('DOMContentLoaded', function() {
    const produtoForm = document.getElementById('produtoForm');

    produtoForm.addEventListener('submit', function(event) {
        event.preventDefault();

        const nome = document.getElementById('nomeProduto').value;
        const preco = parseFloat(document.getElementById('precoProduto').value);
        const descricao = document.getElementById('descricaoProduto').value;
        const imagem = document.getElementById('imagemProduto').files[0];

        // Converte a imagem para Base64 para poder armazenar
        const reader = new FileReader();
        reader.onloadend = function() {
            const imageDataUrl = reader.result;

            // Recupera produtos do localStorage
            const produtos = JSON.parse(localStorage.getItem('produtos')) || [];

            // Adiciona novo produto ao array
            produtos.push({
                nome: nome,
                preco: preco,
                descricao: descricao,
                imagem: imageDataUrl
            });

            // Salva produtos no localStorage
            localStorage.setItem('produtos', JSON.stringify(produtos));

            // Limpa o formulário
            produtoForm.reset();

            alert('Produto adicionado com sucesso!');
        };

        if (imagem) {
            reader.readAsDataURL(imagem);
        }
    });
});
