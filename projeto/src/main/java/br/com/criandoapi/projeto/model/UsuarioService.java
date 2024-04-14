package br.com.criandoapi.projeto.model;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.criandoapi.projeto.DAO.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public Optional<Usuario> encontrarPorUsername(String username) {
		return usuarioRepository.findByUsername(username);
	}
	 public void excluirUsuario(Usuario usuario) {
	        usuarioRepository.delete(usuario);
	    }
}