document.addEventListener('DOMContentLoaded', function() {
	const produtoForm = document.getElementById('produtoForm');

	produtoForm.addEventListener('submit', function(event) {
		event.preventDefault();

		const nome = document.getElementById('nomeProduto').value;
		const preco = parseFloat(document.getElementById('precoProduto').value);
		const descricao = document.getElementById('descricaoProduto').value;
		const imagem = document.getElementById('imagemProduto').files[0];

		// Função para converter imagem para Base64 e redimensionar
		function compressImage(imageFile, callback) {
			const reader = new FileReader();
			reader.onload = function(event) {
				const img = new Image();
				img.onload = function() {
					const canvas = document.createElement('canvas');
					const ctx = canvas.getContext('2d');
					const MAX_WIDTH = 800;
					const MAX_HEIGHT = 800;
					let width = img.width;
					let height = img.height;

					if (width > height) {
						if (width > MAX_WIDTH) {
							height *= MAX_WIDTH / width;
							width = MAX_WIDTH;
						}
					} else {
						if (height > MAX_HEIGHT) {
							width *= MAX_HEIGHT / height;
							height = MAX_HEIGHT;
						}
					}

					canvas.width = width;
					canvas.height = height;
					ctx.drawImage(img, 0, 0, width, height);
					canvas.toBlob(function(blob) {
						callback(blob);
					}, 'image/jpeg', 0.7);
				};
				img.src = event.target.result;
			};
			reader.readAsDataURL(imageFile);
		}

		// Função para adicionar produto ao localStorage
		function adicionarProduto(produto) {
			const produtos = JSON.parse(localStorage.getItem('produtos')) || [];
			produtos.push(produto);
			localStorage.setItem('produtos', JSON.stringify(produtos));
		}

		// Função para enviar dados ao backend
		function enviarDadosBackend(formData) {
			return fetch('http://localhost:8002/produtos', {
				method: 'POST',
				body: formData
			})
				.then(response => {
					if (response.ok) {
						return response.json();
					} else {
						return response.text().then(text => {
							console.error(`Erro HTTP ${response.status}: ${text}`);
							throw new Error(`Erro HTTP ${response.status}: ${text}`);
						});
					}
				});
		}

		const formData = new FormData();
		formData.append('nome', nome);
		formData.append('preco', preco);
		formData.append('descricao', descricao);

		if (imagem) {
			// Quando há imagem, redimensiona e compacta a imagem
			compressImage(imagem, function(compressedImage) {
				formData.append('imagem', compressedImage);

				// Envia os dados ao backend com a imagem
				enviarDadosBackend(formData)
					.then(data => {
						if (data && data.success) {
							adicionarProduto({
								nome: nome,
								preco: preco,
								descricao: descricao,
								imagem: data.imagemURL
							});
							alert('Produto adicionado com sucesso!');
							produtoForm.reset();
						} else {
							alert(`Erro ao adicionar produto: ${data.message || 'Erro desconhecido'}`);
						}
					})
					.catch(error => {
						console.error('Erro ao enviar produto ao backend:', error);
						alert(`Erro ao adicionar produto: ${error.message || 'Erro desconhecido'}`);
					});
			});
		} else {
			// Quando não há imagem, apenas adiciona o produto
			enviarDadosBackend(formData)
				.then(data => {
					if (data && data.success) {
						adicionarProduto({
							nome: nome,
							preco: preco,
							descricao: descricao,
							imagem: null
						});
						alert('Produto adicionado com sucesso!');
						produtoForm.reset();
					} else {
						alert(`Erro ao adicionar produto: ${data.message || 'Erro desconhecido'}`);
					}
				})
				.catch(error => {
					console.error('Erro ao enviar produto ao backend:', error);
					alert(`Erro ao adicionar produto: ${error.message || 'Erro desconhecido'}`);
				});
		}
	});
});