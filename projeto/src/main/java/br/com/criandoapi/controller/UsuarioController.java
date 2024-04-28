package br.com.criandoapi.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.criandoapi.projeto.dto.UsuarioDTO;
import br.com.criandoapi.projeto.model.Usuario;
import br.com.criandoapi.projeto.model.UsuarioService;
import br.com.criandoapi.projeto.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@SuppressWarnings("unused")
	private UsuarioDTO usuario;

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	public ResponseEntity<List<UsuarioDTO>> listaUsuarios() {
		List<Usuario> usuarios = usuarioService.findAll();
		List<UsuarioDTO> usuarioDTOs = usuarios.stream().map(usuarioService::convertToDTO).collect(Collectors.toList());
		return ResponseEntity.ok(usuarioDTOs);
	}

	@PostMapping
	@Transactional
	public ResponseEntity<?> criarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String mensagemErro = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage)
					.collect(Collectors.joining("; "));
			return ResponseEntity.badRequest().body(mensagemErro);
		}

		ResponseEntity<?> campoInvalidoResponse = usuarioService.validarCampo(usuarioDTO);
		if (campoInvalidoResponse != null) {
			return campoInvalidoResponse;
		}

		try {
			if (!usuarioService.contemNumeros(usuarioDTO.getNome())) {
				return ResponseEntity.badRequest()
						.body("{\"error\":\"O nome não pode conter números ou caracteres especiais.\"}");
			}
			if (!usuarioService.formaDeEmailCorreto(usuarioDTO.getEmail())) {
				return ResponseEntity.badRequest().body("{\"error\":\"O e-mail está escrito de forma incorreta.\"}");
			}
			if (!usuarioService.apenasNumeros(usuarioDTO.getTelefone())) {
				return ResponseEntity.badRequest()
						.body("{\"error\":\"O campo telefone não pode conter letras ou caracteres especiais.\"}");
			}
			if (usuarioService.existeEmail(usuarioDTO.getEmail())) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"error\":\"O e-mail já está em uso.\"}");
			}
			if (usuarioService.existeTelefone(usuarioDTO.getTelefone())) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"error\":\"O telefone já está em uso.\"}");
			}
			if (usuarioService.existeUsername(usuarioDTO.getUsername())) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"error\":\"O username já está em uso.\"}");
			}
			Usuario usuario = usuarioService.convertToEntity(usuarioDTO);
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String senhaCriptografada = encoder.encode(usuarioDTO.getSenha());
			usuario.setSenha(senhaCriptografada);
			Usuario usuarioSalvo = usuarioService.save(usuario);
			UsuarioDTO usuarioSalvoDTO = usuarioService.convertToDTO(usuarioSalvo);
			return new ResponseEntity<>(usuarioSalvoDTO, HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro de integridade de dados: " + e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro interno no servidor: " + e.getMessage());
		}
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<?> editarUsuario(@PathVariable Integer id, @RequestBody UsuarioDTO usuarioDTO) {
		Optional<Usuario> usuarioOptional = repository.findById(id);
		if (!usuarioOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		ResponseEntity<?> campoInvalidoResponse = usuarioService.validarCampo(usuarioDTO);
		if (campoInvalidoResponse != null) {
			return campoInvalidoResponse;
		}

		if (!usuarioService.contemNumeros(usuarioDTO.getNome())) {
			return ResponseEntity.badRequest()
					.body("{\"error\":\"O nome não pode conter números ou caracteres especiais.\"}");
		}
		if (!usuarioService.formaDeEmailCorreto(usuarioDTO.getEmail())) {
			return ResponseEntity.badRequest().body("{\"error\":\"O e-mail está escrito de forma incorreta.\"}");
		}
		if (!usuarioService.apenasNumeros(usuarioDTO.getTelefone())) {
			return ResponseEntity.badRequest()
					.body("{\"error\":\"O campo telefone não pode conter letras ou caracteres especiais.\"}");
		}

		Usuario usuarioAtual = usuarioOptional.get();
		ResponseEntity<?> unicidadeResponse = usuarioService.verificarUnicidadeDeUsuario(usuarioDTO, usuarioAtual);
		if (unicidadeResponse != null) {
			return unicidadeResponse;
		}
		usuarioAtual.setNome(usuarioDTO.getNome());
		usuarioAtual.setUsername(usuarioDTO.getUsername());
		usuarioAtual.setEmail(usuarioDTO.getEmail());
		usuarioAtual.setTelefone(usuarioDTO.getTelefone());

		if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().isEmpty()) {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String senhaCriptografada = encoder.encode(usuarioDTO.getSenha());
			usuarioAtual.setSenha(senhaCriptografada);
		}
		Usuario usuarioAtualizado = repository.save(usuarioAtual);
		UsuarioDTO usuarioAtualizadoDTO = usuarioService.convertToDTO(usuarioAtualizado);
		return ResponseEntity.ok(usuarioAtualizadoDTO);
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUsuario(@RequestBody Map<String, String> loginRequest) {
		String username = loginRequest.get("username");
		String senha = loginRequest.get("senha");

		if (username == null || senha == null) {
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap("erro", "Username e senha são obrigatórios"));
		}

		Optional<Usuario> usuarioOptional = usuarioService.encontrarPorUsername(username);

		if (usuarioOptional.isPresent()) {
			Usuario usuario = usuarioOptional.get();
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			if (encoder.matches(senha, usuario.getSenha())) {
				return ResponseEntity.ok().body(Collections.singletonMap("mensagem", "Login realizado com sucesso."));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("erro",
						"Senha incorreta ou não foi fornecida. Por favor, verifique."));
			}
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("erro",
					"Usuário não encontrado. Por favor, cadastre-se primeiro ou verifique suas credenciais e tente novamente."));
		}
	}

	@DeleteMapping("/contadelete/{username}")
	public ResponseEntity<?> excluirUsuarioPorNome(@PathVariable String username) {
		Optional<Usuario> usuarioOptional = usuarioService.encontrarPorUsername(username);
		if (usuarioOptional.isPresent()) {
			usuarioService.excluirUsuario(usuarioOptional.get());
			return ResponseEntity.status(204).body("Conta excluída com sucesso!");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
		}
	}
}