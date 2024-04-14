package br.com.criandoapi.projeto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"br.com.criandoapi.controller","br.com.criandoapi.projeto.DAO","br.com.criandoapi.projeto.model","securityconfig"})
public class ProjetoApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProjetoApplication.class, args);
		
		System.out.println("\u001B[32mServidor conectado: \u001B[0m");
	}
}