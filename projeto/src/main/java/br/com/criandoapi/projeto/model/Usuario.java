package br.com.criandoapi.projeto.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="usuario")
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Integer id;
	
	@Column(name="nome_completo", length = 200)
	@NotEmpty(message = "O campo nome não pode estar em braco")
	private String nome;
	
	@Column(name="username", length = 20)
	@NotBlank(message = "O username não pode estar em branco")
	private String username;
	
	
	@Column(name="email", length = 50)
	@NotBlank(message = "O email não pode estar em branco")
	@Email(message = "O email inserido não é válido")
	private String email;
	
	@Column(name="senha", columnDefinition = "TEXT")
	@NotBlank(message = "A senha não pode estar em branco")
	@Size(min = 6, max = 20, message = "A senha deve ter entre 6 e 20 caracteres")
	private String senha;
	
	@Column(name="telefone", length = 15)
	@NotBlank(message = "O telefone não pode estar em branco")
	private String telefone;	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
}