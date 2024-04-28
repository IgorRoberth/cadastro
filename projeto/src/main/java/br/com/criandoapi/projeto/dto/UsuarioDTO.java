package br.com.criandoapi.projeto.dto;

import javax.validation.constraints.NotBlank;

import br.com.criandoapi.projeto.model.Usuario;

public class UsuarioDTO {

    private Integer id;

    @NotBlank(message = "O campo nome não pode estar em branco")
    private String nome;

    @NotBlank(message = "O username não pode estar em branco")
    private String username;

    @NotBlank(message = "O email não pode estar em branco")
    private String email;

    @NotBlank(message = "A senha não pode estar em branco")
    private String senha;

    @NotBlank(message = "O telefone não pode estar em branco")
    private String telefone;

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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario toUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNome(this.nome);
        usuario.setUsername(this.username);
        usuario.setEmail(this.email);
        usuario.setSenha(this.senha);
        usuario.setTelefone(this.telefone);
        return usuario;
    }
}