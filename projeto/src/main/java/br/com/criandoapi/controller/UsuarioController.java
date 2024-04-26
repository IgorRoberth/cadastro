package br.com.criandoapi.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.criandoapi.projeto.model.Usuario;
import br.com.criandoapi.projeto.model.UsuarioService;
import br.com.criandoapi.projeto.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioRepository repository;

	@Autowired
	private UsuarioService usuarioService;

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<?> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
				.body("Este endpoint suporta apenas solicitações POST.");
	}

	@GetMapping
	public ResponseEntity<Iterable<Usuario>> listaUsuarios() {
		Iterable<Usuario> usuarios = repository.findAll();
		return ResponseEntity.status(200).body(usuarios);
	}
	
	private boolean contemNumeros(String texto) {
		return texto != null && texto.matches(".*\\d.*");
	}

	private boolean formaDeEmailCorreto(String email) {
	    if (email == null) {
	        return false;
	    }
	    String regex = "^[A-Za-z0-9+_.-]+@(gmail\\.com|bol\\.com|yahoo\\.com|hotmail\\.com|outlook\\.com|email\\.com)(\\.br)?$";
	    return email.matches(regex);
	}	
	
	private boolean apenasNumeros(String telefone) {
	    return telefone != null && telefone.matches("[0-9]+");
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<?> criarUsuario(@Valid @RequestBody Usuario usuario, BindingResult bindingResult) {
	    if (bindingResult.hasErrors()) {
	        String mensagemErro = bindingResult.getAllErrors().stream()
	                .map(ObjectError::getDefaultMessage)
	                .collect(Collectors.joining("; "));
	        return ResponseEntity.badRequest().body(mensagemErro);
	    }

	    if (usuario.getNome() == null || usuario.getNome().isEmpty() ||
	        usuario.getUsername() == null || usuario.getUsername().isEmpty() ||
	        usuario.getEmail() == null || usuario.getEmail().isEmpty() ||
	        usuario.getSenha() == null || usuario.getSenha().isEmpty() ||
	        usuario.getTelefone() == null || usuario.getTelefone().isEmpty()) {
	        return ResponseEntity.badRequest().body("Para concluir o cadastro é necessário preencher todos os campos.");
	    }

	    if (contemNumeros(usuario.getNome())) {
	        return ResponseEntity.badRequest().body("O nome não pode conter números.");
	    }

	    if (!formaDeEmailCorreto(usuario.getEmail())) {
	        return ResponseEntity.badRequest().body("O e-mail está escrito de forma incorreta.");
	    }
	    
	    if(!apenasNumeros(usuario.getTelefone())) {
	        return ResponseEntity.badRequest().body("O campo telefone não pode conter letras ou qualquer tipo de caracteres especiais.");	    	
	    }
	    
	    String campoRepetido = validarCamposUnicos(usuario);
	    if (campoRepetido != null) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(Collections.singletonMap("error", campoRepetido));
	    }

	    try {
	        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	        String senhaCriptografada = encoder.encode(usuario.getSenha());
	        usuario.setSenha(senhaCriptografada);
	        Usuario usuarioNovo = repository.save(usuario);
	        return new ResponseEntity<>(usuarioNovo, HttpStatus.CREATED);
	    } catch (Exception e) {
	        Map<String, String> responseBody = new HashMap<>();
	        responseBody.put("error", "Erro interno");
	        responseBody.put("message", "Erro interno no servidor. Detalhes: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
	    }
	}

	private String validarCamposUnicos(Usuario usuario) {
	    if (repository.existsByUsername(usuario.getUsername())) {
	        return "O username já está em uso por outro usuário.";
	    }
	    if (repository.existsByEmail(usuario.getEmail())) {
	        return "O email já está em uso por outro usuário.";
	    }
	    if (repository.existsByTelefone(usuario.getTelefone())) {
	        return "O telefone já está em uso por outro usuário.";
	    }
	    return null;
	}


	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<?> editarUsuario(@PathVariable Integer id, @RequestBody Usuario usuario) {
		Optional<Usuario> usuarioOptional = repository.findById(id);
		if (usuarioOptional.isPresent()) {
			Usuario usuarioAtual = usuarioOptional.get();
			
			if (usuario.getNome() == null || usuario.getNome().isEmpty() ||
			        usuario.getUsername() == null || usuario.getUsername().isEmpty() ||
			        usuario.getEmail() == null || usuario.getEmail().isEmpty() ||
			        usuario.getSenha() == null || usuario.getSenha().isEmpty() ||
			        usuario.getTelefone() == null || usuario.getTelefone().isEmpty()) {
			        return ResponseEntity.badRequest().body("Para concluir atualização dos novos dados é necessário preencher todos os campos.");
			   }

			if (contemNumeros(usuario.getNome())) {
				return ResponseEntity.badRequest().body("O nome não pode conter números.");
			}
			if (!formaDeEmailCorreto(usuario.getEmail())) {
				return ResponseEntity.badRequest().body("O e-mail está escrito de forma incorreta.");
			}
			if(!apenasNumeros(usuario.getTelefone())) {
		           return ResponseEntity.badRequest().body("O campo telefone não pode conter letras ou qualquer tipo de caracteres especiais.");	    	
			    }
			if (dadosIguais(usuarioAtual, usuario)) {
				return ResponseEntity.badRequest().body("Nenhuma alteração foi detectada nos dados fornecidos.");
			}

			usuarioAtual.setNome(usuario.getNome());
			usuarioAtual.setUsername(usuario.getUsername());
			usuarioAtual.setEmail(usuario.getEmail());
			usuarioAtual.setTelefone(usuario.getTelefone());

			if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()
					&& !senhaNaoModificada(usuarioAtual, usuario)) {
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				String senhaCriptografada = encoder.encode(usuario.getSenha());
				usuarioAtual.setSenha(senhaCriptografada);
			}
			Usuario usuarioAtualizado = repository.save(usuarioAtual);
			return ResponseEntity.ok().body(usuarioAtualizado);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	private boolean dadosIguais(Usuario usuarioAtual, Usuario usuarioNovo) {
		boolean nomeIgual = usuarioAtual.getNome().equals(usuarioNovo.getNome());
		boolean usernameIgual = usuarioAtual.getUsername().equals(usuarioNovo.getUsername());
		boolean emailIgual = usuarioAtual.getEmail().equals(usuarioNovo.getEmail());
		boolean telefoneIgual = usuarioAtual.getTelefone().equals(usuarioNovo.getTelefone());
		boolean senhaIgual = (usuarioNovo.getSenha() == null || usuarioNovo.getSenha().isEmpty()
				|| senhaNaoModificada(usuarioAtual, usuarioNovo));
		return nomeIgual && usernameIgual && emailIgual && telefoneIgual && senhaIgual;
	}

	private boolean senhaNaoModificada(Usuario usuarioAtual, Usuario usuarioNovo) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(usuarioNovo.getSenha(), usuarioAtual.getSenha());
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
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Collections.singletonMap("erro", "Senha incorreta ou não foi fornecida. Por favor, verifique."));
			}
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("erro", "Usuário não encontrado. Por favor, cadastre-se primeiro ou verifique suas credenciais e tente novamente."));
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