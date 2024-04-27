package br.com.criandoapi.projeto.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.criandoapi.projeto.dto.UsuarioDTO;
import br.com.criandoapi.projeto.repositories.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	public Optional<Usuario> encontrarPorUsername(String username) {
		return usuarioRepository.findByUsername(username);
	}

	public boolean existeEmail(String email) {
		return usuarioRepository.existsByEmail(email);
	}

	public boolean existeTelefone(String telefone) {
		return usuarioRepository.existsByTelefone(telefone);
	}

	public boolean existeUsername(String username) {
		return usuarioRepository.existsByUsername(username);
	}

	public void excluirUsuario(Usuario usuario) {
		usuarioRepository.delete(usuario);
	}

	public List<Usuario> findAll() {
		return (List<Usuario>) usuarioRepository.findAll();
	}

	public Usuario convertToEntity(UsuarioDTO usuarioDTO) {
		Usuario usuario = new Usuario();
		usuario.setNome(usuarioDTO.getNome());
		usuario.setUsername(usuarioDTO.getUsername());
		usuario.setEmail(usuarioDTO.getEmail());
		usuario.setTelefone(usuarioDTO.getTelefone());
		return usuario;
	}

	public Usuario save(Usuario usuario) {
		return usuarioRepository.save(usuario);
	}

	public UsuarioDTO convertToDTO(Usuario usuario) {
		UsuarioDTO usuarioDTO = new UsuarioDTO();
		usuarioDTO.setId(usuario.getId());
		usuarioDTO.setNome(usuario.getNome());
		usuarioDTO.setUsername(usuario.getUsername());
		usuarioDTO.setEmail(usuario.getEmail());
		usuarioDTO.setTelefone(usuario.getTelefone());
		return usuarioDTO;
	}
}