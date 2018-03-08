package br.com.toto.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.toto.dto.Erro;
import br.com.toto.model.Jogador;
import br.com.toto.repository.JogadorRepository;

@RestController
@RequestMapping("/jogador")
public class JogadorController {

	@Autowired
	private JogadorRepository repositorio;
	
	public static final Logger logger = LoggerFactory.getLogger(JogadorController.class);
	
	@PostMapping
	public ResponseEntity<?> criarJogador(@RequestBody Jogador jogador, UriComponentsBuilder builder){
		logger.info("Cadastrando jogador...");
		Optional<Jogador> jogadorOptional = repositorio.findByEmail(jogador.getEmail());
		if (jogadorOptional.isPresent()) {
			Erro erro = new Erro("Já existe um jogador com este e-mail");
			logger.info(erro.getMensagem());
			return new ResponseEntity<Erro>(erro, HttpStatus.CONFLICT);
		}
		Jogador novoJogador = repositorio.save(jogador);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(builder.path("/jogador/{id}").buildAndExpand(novoJogador.getId()).toUri());
		logger.info("Jogador {} cadastrado com sucesso",jogador.getNome());
		return new ResponseEntity<Jogador>(jogador, headers, HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<?> listarJogadores(){
		List<Jogador> jogadores = repositorio.findAll();
		return new ResponseEntity<List<Jogador>>(jogadores, HttpStatus.OK);
	}
	
}
