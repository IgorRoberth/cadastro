package br.com.criandoapi.controller;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.criandoapi.projeto.DAO.IUsuario;
import br.com.criandoapi.projeto.model.Usuario;
import br.com.criandoapi.projeto.model.UsuarioService;

@RestController
@CrossOrigin("*")
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private IUsuario dao;

	@Autowired
	private UsuarioService usuarioService;

	@GetMapping
	public ResponseEntity<Iterable<Usuario>> listaUsuarios() {
		Iterable<Usuario> usuarios = dao.findAll();
		return ResponseEntity.ok().body(usuarios);
	}

	@PostMapping
	public ResponseEntity<?> criarUsuario(@Valid @RequestBody Usuario usuario, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			String mensagemErro = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage)
					.collect(Collectors.joining("; "));
			return ResponseEntity.badRequest().body(mensagemErro);
		}
		try {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			String senhaCriptografada = encoder.encode(usuario.getSenha());
			usuario.setSenha(senhaCriptografada);
			Usuario usuarioNovo = dao.save(usuario);
			return new ResponseEntity<>(usuarioNovo, HttpStatus.CREATED);
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("O e-mail já está em uso.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno no servidor.");
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<Usuario> editarUsuario(@PathVariable Integer id, @RequestBody Usuario usuario) {
		Optional<Usuario> usuarioOptional = dao.findById(id);
		if (usuarioOptional.isPresent()) {
			Usuario usuarioEditado = usuarioOptional.get();
			usuarioEditado.setNome(usuario.getNome());
			usuarioEditado.setUsername(usuario.getUsername());
			usuarioEditado.setEmail(usuario.getEmail());
			usuarioEditado.setTelefone(usuario.getTelefone());
			if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				String senhaCriptografada = encoder.encode(usuario.getSenha());
				usuarioEditado.setSenha(senhaCriptografada);
			}
			Usuario usuarioAtualizado = dao.save(usuarioEditado);
			return ResponseEntity.ok().body(usuarioAtualizado);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluirUsuario(@PathVariable Integer id) {
		Optional<Usuario> usuarioOptional = dao.findById(id);
		if (usuarioOptional.isPresent()) {
			dao.deleteById(id);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/check-usuario")
	public ResponseEntity<?> checkUsuario(@RequestParam(required = false) String username,
			@RequestParam(required = false) String senha) {
		if (username == null || senha == null) {
			return ResponseEntity.badRequest()
					.body(Collections.singletonMap("erro", "Nome de usuário e senha são obrigatórios"));
		}

		Optional<Usuario> usuarioOptional = usuarioService.encontrarPorUsername(username);

		if (usuarioOptional.isPresent()) {
			Usuario usuario = usuarioOptional.get();
			if (usuario.getSenha().equals(senha)) {
				return ResponseEntity.status(HttpStatusCode.valueOf(200))
						.body(Collections.singletonMap("Sucesso", "Login realizado com sucesso"));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(Collections.singletonMap("erro", "Senha incorreta"));
			}
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(Collections.singletonMap("erro", "Usuário não cadastrado"));
		}
	}
}