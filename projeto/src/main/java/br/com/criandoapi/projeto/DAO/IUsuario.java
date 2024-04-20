package br.com.criandoapi.projeto.DAO;

import org.springframework.data.repository.CrudRepository;
import br.com.criandoapi.projeto.model.Usuario;

public interface IUsuario extends CrudRepository<Usuario, Integer> {

	boolean existsByEmail(String email);
	boolean existsByTelefone(String telefone);
	boolean existsByUsername(String username);	
}
