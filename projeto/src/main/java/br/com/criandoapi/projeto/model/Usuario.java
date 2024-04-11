package br.com.criandoapi.projeto.model;

import javax.validation.constraints.NotBlank;

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
	
	@Column(name="nome_completo", length = 200, nullable = true)
	@NotBlank(message = "O nome não pode estar em branco")
	private String nome;
	
	@Column(name="username", length = 20, nullable = true)
	@NotBlank(message = "O username não pode estar em branco")
	private String username;
	
	
	@Column(name="email", length = 50, nullable = true)
	@NotBlank(message = "O email não pode estar em branco")
	private String email;
	
	@Column(name="senha", columnDefinition = "TEXT", nullable = true)
	@NotBlank(message = "A senha não pode estar em branco")
	private String senha;
	
	@Column(name="telefone", length = 15, nullable = true)
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
