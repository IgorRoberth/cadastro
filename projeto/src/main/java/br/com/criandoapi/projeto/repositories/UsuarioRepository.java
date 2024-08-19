package br.com.criandoapi.projeto.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.criandoapi.projeto.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	Optional<Usuario> findByUsername(String username);
	Usuario findByEmail(String email);
	boolean existsByEmail(String email);
	boolean existsByTelefone(String telefone);
	boolean existsByUsername(String username);
	Optional<Usuario> findById(Long id);

}