package com.biblioteca.biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Esta e a classe principal da aplicacao.
//
// Quando executamos este arquivo, o Spring Boot inicia o projeto inteiro:
// 1. sobe o contexto do Spring
// 2. procura beans nos pacotes corretos
// 3. aplica configuracoes automaticas
// 4. inicia o servidor web embutido
//
// Esta classe fica no pacote raiz do projeto de proposito.
// Isso e importante porque o Spring faz o component scan a partir daqui,
// encontrando classes anotadas em subpacotes como:
// - controller
// - service
// - repository
// - config
//
// Em outras palavras:
// se uma classe estiver em um subpacote de com.biblioteca.biblioteca
// e estiver anotada corretamente, o Spring normalmente consegue encontra-la.
@SpringBootApplication
public class BibliotecaApplication {

	public static void main(String[] args) {
		// Este e o ponto de entrada da aplicacao Java.
		//
		// SpringApplication.run(...)
		// cria o container do Spring, registra beans, aplica configuracoes
		// e inicializa a aplicacao.
		//
		// Aqui comeca a Inversao de Controle na pratica:
		// depois que o Spring sobe, ele passa a gerenciar os objetos principais
		// da aplicacao, como controllers, services e repositories.
		SpringApplication.run(BibliotecaApplication.class, args);
	}

}

