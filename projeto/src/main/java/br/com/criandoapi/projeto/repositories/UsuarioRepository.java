package br.com.criandoapi.projeto.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.criandoapi.projeto.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	Optional<Usuario> findByUsername(String username);
	boolean existsByEmail(String email);
	boolean existsByTelefone(String telefone);
	boolean existsByUsername(String username);	
 
}

