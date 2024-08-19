package br.com.criandoapi.projeto.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.criandoapi.projeto.model.Produtos;

@Repository
public interface ProdutoRepository extends JpaRepository<Produtos, Long> {
    // Métodos personalizados podem ser adicionados aqui, se necessário.
}
