document.addEventListener('DOMContentLoaded', function() {
    const productList = document.getElementById('productList');

    // Função para exibir produtos na página
    function displayProduct(product) {
        const productItem = document.createElement('div');
        productItem.classList.add('product-item');
        productItem.innerHTML = `
            <img src="data:image/jpeg;base64,${product.imagem}" alt="Imagem do produto">
            <h3>${product.nome}</h3>
            <p>Preço: R$ ${product.preco.toFixed(2)}</p>
            <p>${product.descricao}</p>
        `;
        productList.appendChild(productItem);
    }

    // Função para recuperar produtos do back-end
    function fetchProducts() {
        fetch('http://localhost:8002/produtos') // Ajuste a URL conforme necessário
            .then(response => response.json())
            .then(products => {
                products.forEach(displayProduct);
            })
            .catch(error => console.error('Erro ao recuperar produtos:', error));
    }

    // Chama a função para recuperar produtos do back-end
    fetchProducts();
});	