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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.toto.model.Campeonato;
import br.com.toto.model.Jogador;
import br.com.toto.repository.CampeonatoRepository;
import br.com.toto.repository.JogadorRepository;
import br.com.toto.utils.StatusCampeonato;

@RestController
@RequestMapping("/campeonatos")
public class CampeonatoController {

	@Autowired
	private CampeonatoRepository repositorio;
	
	@Autowired
	private JogadorRepository repositorioJogador;
	
	public static final Logger logger = LoggerFactory.getLogger(CampeonatoController.class);
	
	@PostMapping
	public ResponseEntity<?> criarCampeonato(@RequestBody Campeonato campeonato, UriComponentsBuilder ucBuilder ){
		logger.info("Cadastrando campeonato...");
		campeonato.setStatus(StatusCampeonato.EM_CRIACAO);
		Campeonato campeonatoSalvo = repositorio.save(campeonato);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/campeonatos/{id}").buildAndExpand(campeonatoSalvo.getId()).toUri());
		logger.info("Campeonato cadastrado com sucesso!");
		return new ResponseEntity<Campeonato>(campeonatoSalvo, headers, HttpStatus.CREATED);				
	}
	
	@GetMapping
	public ResponseEntity<?> listarCampeonatos() {
		logger.info("Buscando campeonatos...");
		List<Campeonato> campeonatos = repositorio.findAll();
		if (campeonatos.size() > 0) {
			logger.info("{} campeonatos encontrados!",campeonatos.size());
			return new ResponseEntity<List<Campeonato>>(campeonatos, HttpStatus.OK);
		}
		logger.info("Nenhum campeonato encontrado!");
		return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<?> buscarCampeonatoPorId(@PathVariable("id") Integer id) {
		logger.info("Buscando campeonato com id:{}", id);
		Optional<Campeonato> campeonato = repositorio.findById(id);
		if (campeonato.isPresent()) {
			logger.info("Campeonato com id {} encontrado", id);
			return new ResponseEntity<Campeonato>(campeonato.get(), HttpStatus.OK);	
		}
		logger.info("Campeonato com id {} não encontrado", id);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping(value="/{id}/jogador")
	public ResponseEntity<?> inscreverJogadorNoCampeonato(@PathVariable("id") Integer id, @RequestBody(required=true) Jogador jogador){
		logger.info("Inscrevendo jogador no campeonato...");
		Optional<Campeonato> campeonatoOptional = repositorio.findById(id);
		Campeonato campeonato;
		if (campeonatoOptional.isPresent()) {
			campeonato = campeonatoOptional.get();
			Optional<Jogador> jogadorOptional = repositorioJogador.findByEmail(jogador.getEmail());
			Jogador novoJogador;
			if (!jogadorOptional.isPresent()) {
				novoJogador = repositorioJogador.save(jogador);
				campeonato.getJogadores().add(novoJogador);
				logger.info("O jogador [{}] foi cadastrado no campeonato [{}]", jogador.getNome(), campeonato.getNome());
			}else {
				campeonato.getJogadores().add(jogadorOptional.get());
			}
			repositorio.save(campeonato);
		}else {
			return new ResponseEntity<>("Campeonato com id ["+id+"] nao foi encontrado", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Campeonato>(campeonato, HttpStatus.CREATED);
	}
}