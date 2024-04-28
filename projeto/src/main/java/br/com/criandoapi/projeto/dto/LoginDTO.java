package br.com.criandoapi.projeto.dto;

public class LoginDTO {
    private String username;
    private String senha;

    public String getLogin() {
        return username;
    }

    public void setLogin(String username) {
        this.username = username;
    }

    public String getSenhaDTO() {
        return senha;
    }

    public void setSenhaDTO(String senha) {
        this.senha = senha;
    }
}
