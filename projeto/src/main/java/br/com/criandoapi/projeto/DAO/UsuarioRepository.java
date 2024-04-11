package br.com.criandoapi.projeto.DAO;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.criandoapi.projeto.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUsername(String username);
}

