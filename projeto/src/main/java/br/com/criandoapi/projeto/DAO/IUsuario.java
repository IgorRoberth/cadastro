package br.com.criandoapi.projeto.DAO;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import br.com.criandoapi.projeto.model.Usuario;

public interface IUsuario extends CrudRepository<Usuario, Integer> {

	Optional<Usuario> findByTelefone(String telefone);
	boolean existsByEmail(String email);
	boolean existsByTelefone(String telefone);
	boolean existsByUsername(String username);	
}
