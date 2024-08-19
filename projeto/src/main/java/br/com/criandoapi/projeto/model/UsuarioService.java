package br.com.criandoapi.projeto.model;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import br.com.criandoapi.projeto.dto.UsuarioDTO;
import br.com.criandoapi.projeto.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;

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

	@Transactional
	public ResponseEntity<?> validarCampo(@Valid @NotNull UsuarioDTO usuarioDTO) {
		if (usuarioDTO.getNome() == null || usuarioDTO.getNome().isEmpty() || usuarioDTO.getUsername() == null
				|| usuarioDTO.getUsername().isEmpty() || usuarioDTO.getEmail() == null
				|| usuarioDTO.getEmail().isEmpty() || usuarioDTO.getSenha() == null || usuarioDTO.getSenha().isEmpty()
				|| usuarioDTO.getTelefone() == null || usuarioDTO.getTelefone().isEmpty()) {
			return ResponseEntity.badRequest().body("Para concluir o cadastro é necessário preencher todos os campos.");
		}
		return null;

	}

	public ResponseEntity<?> criarUsuario(UsuarioDTO usuarioDTO) {
		ResponseEntity<?> campoInvalidoResponse = validarCampo(usuarioDTO);
		if (campoInvalidoResponse != null) {
			return campoInvalidoResponse;
		}

		validarCampos(usuarioDTO);
		validarCamposUnicos(usuarioDTO);

		try {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String senhaCriptografada = encoder.encode(usuarioDTO.getSenha());
			usuarioDTO.setSenha(senhaCriptografada);
			Usuario usuario = usuarioDTO.toUsuario();
			Usuario usuarioNovo = usuarioRepository.save(usuario);
			return new ResponseEntity<>(usuarioNovo, HttpStatus.CREATED);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro interno no servidor. Detalhes: " + e.getMessage());
		}
	}

	public void validarCampos(@Valid UsuarioDTO usuarioDTO) {
		if (contemNumeros(usuarioDTO.getNome())) {
			throw new IllegalArgumentException("O nome não pode conter números.");
		}
	}

	public void validarCamposUnicos(@Valid UsuarioDTO usuarioDTO) {
		if (usuarioRepository.existsByUsername(usuarioDTO.getUsername())) {
			throw new IllegalArgumentException("O username já está em uso por outro usuário.");
		}
		if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
			throw new IllegalArgumentException("O email já está em uso por outro usuário.");
		}
		if (usuarioRepository.existsByTelefone(usuarioDTO.getTelefone())) {
			throw new IllegalArgumentException("O telefone já está em uso por outro usuário.");
		}
	}

	public ResponseEntity<?> verificarUnicidadeDeUsuario(UsuarioDTO usuarioDTO, Usuario usuarioAtual) {
		if (!usuarioDTO.getEmail().equals(usuarioAtual.getEmail()) && existeEmail(usuarioDTO.getEmail())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"error\":\"O e-mail já está em uso.\"}");
		}
		if (!usuarioDTO.getTelefone().equals(usuarioAtual.getTelefone()) && existeTelefone(usuarioDTO.getTelefone())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"error\":\"O telefone já está em uso.\"}");
		}
		if (!usuarioDTO.getUsername().equals(usuarioAtual.getUsername()) && existeUsername(usuarioDTO.getUsername())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"error\":\"O username já está em uso.\"}");
		}
		return null;
	}

	public boolean contemNumeros(String texto) {
		return texto != null && texto.matches("[a-zA-ZÀ-ÿ\\s]+");
	}

	public boolean formaDeEmailCorreto(String email) {
		if (email == null) {
			return false;
		}
		String regex = "^[A-Za-z0-9+_.-]+@(gmail\\.com|bol\\.com|yahoo\\.com|hotmail\\.com|outlook\\.com|email\\.com)(\\.br)?$";
		return email.matches(regex);
	}

	public boolean apenasNumeros(String telefone) {
		return telefone != null && telefone.matches("[0-9]+");
	}

	public static String validarSenha(String senha) {
		if (senha.length() < 8) {
			return "A senha deve ter pelo menos 8 carácteres.";
		} else if (!senha.matches(".*[A-Z].*")) {
			return "A senha deve conter pelo menos uma letra maiúscula.";
		} else if (!senha.matches(".*[a-z].*")) {
			return "A senha deve conter pelo menos uma letra minúscula.";
		} else if (!senha.matches(".*[0-9].*")) {
			return "A senha deve conter pelo menos um número.";
		} else if (!senha.matches(".*[!@#$%^&*()-+=].*")) {
			return "A senha deve conter pelo menos um caráctere especial.";
		}
		return null;
	}
}