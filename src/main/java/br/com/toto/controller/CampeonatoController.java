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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.toto.dto.Erro;
import br.com.toto.exception.JogadoresInsuficientesException;
import br.com.toto.exception.NumeroDeJogadoresImparException;
import br.com.toto.model.Campeonato;
import br.com.toto.model.Jogador;
import br.com.toto.repository.CampeonatoRepository;
import br.com.toto.repository.JogadorRepository;
import br.com.toto.utils.BrasilTimesSerieA2018Enum;
import br.com.toto.utils.StatusCampeonato;

@RestController
@RequestMapping("/campeonato")
public class CampeonatoController {

	@Autowired
	private CampeonatoRepository repositorio;
	
	@Autowired
	private JogadorRepository repositorioJogador;
	
	public static final Logger logger = LoggerFactory.getLogger(CampeonatoController.class);
	
	@PostMapping
	public ResponseEntity<?> criarCampeonato(@RequestBody Campeonato campeonato, UriComponentsBuilder ucBuilder ){
		logger.info("Cadastrando campeonato...");
		Optional<Campeonato> campeonatoOptional = repositorio.findByNome(campeonato.getNome());
		if (campeonatoOptional.isPresent()) {
			Erro erro = new Erro("Já existe um campeonato com este nome");
			logger.info(erro.getMensagem());
			return new ResponseEntity<Erro>(erro, HttpStatus.CONFLICT);
		}
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
		Erro erro = new Erro("Nenhum campeonato encontrado");
		logger.info(erro.getMensagem());
		return new ResponseEntity<Erro>(erro, HttpStatus.NOT_FOUND); 
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<?> buscarCampeonatoPorId(@PathVariable("id") Integer id) {
		logger.info("Buscando campeonato com id:{}", id);
		Optional<Campeonato> campeonato = repositorio.findById(id);
		if (campeonato.isPresent()) {
			logger.info("Campeonato com id {} encontrado", id);
			return new ResponseEntity<Campeonato>(campeonato.get(), HttpStatus.OK);	
		}
		Erro erro = new Erro("Campeonato com id: "+id+" não encontrado");
		logger.info(erro.getMensagem());
		return new ResponseEntity<Erro>(erro, HttpStatus.NOT_FOUND);
	}
	
	@PostMapping(value="/{idCampeonato}/jogador")
	public ResponseEntity<?> inscreverJogadorNoCampeonato(@PathVariable("idCampeonato") Integer idCampeonato, @RequestBody(required=true) Jogador jogador){
		logger.info("Inscrevendo jogador no campeonato...");
		Optional<Campeonato> campeonatoOptional = repositorio.findById(idCampeonato);
		Campeonato campeonato;
		if (campeonatoOptional.isPresent()) {
			campeonato = campeonatoOptional.get();
			Optional<Jogador> jogadorOptional = repositorioJogador.findByEmail(jogador.getEmail());
			Jogador novoJogador;
			if (jogadorOptional.isPresent()) {
				campeonato.getJogadores().add(jogadorOptional.get());
				logger.info("O jogador [{}] já existia e foi inscrito no campeonato [{}]", jogador.getNome(), campeonato.getNome());
			}else {
				novoJogador = repositorioJogador.save(jogador);
				campeonato.getJogadores().add(novoJogador);
				logger.info("O jogador [{}] foi inscrito no campeonato [{}]", jogador.getNome(), campeonato.getNome());
			}
			repositorio.save(campeonato);
		}else {
			Erro erro = new Erro("Campeonato com id: "+idCampeonato+" não encontrado");
			logger.info(erro.getMensagem());
			return new ResponseEntity<Erro>(erro, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Campeonato>(campeonato, HttpStatus.CREATED);
	}
	
	@PutMapping(value="/{idCampeonato}/jogador/{idJogador}")
	public ResponseEntity<?> removerJogadorDoCampeonato(@PathVariable("idCampeonato") Integer idCampeonato, @PathVariable("idJogador") Integer idJogador){
		Optional<Campeonato> campeonatoOptional = repositorio.findById(idCampeonato);
		if (campeonatoOptional.isPresent()) {
			Optional<Jogador> jogadorOptional = repositorioJogador.findById(idJogador);
			if (jogadorOptional.isPresent()) {
				Campeonato campeonato = campeonatoOptional.get();
				campeonato.getJogadores().remove(jogadorOptional.get());
				repositorio.save(campeonato);
				return new ResponseEntity<Campeonato>(campeonato, HttpStatus.OK); 
			}else {
				Erro erro = new Erro("Jogador com id: "+idJogador+" não encontrado");
				logger.info(erro.getMensagem());
				return new ResponseEntity<Erro>(erro, HttpStatus.NOT_FOUND);
			}			
		}
		Erro erro = new Erro("Campeonato com id: "+idCampeonato+" não encontrado");
		logger.info(erro.getMensagem());
		return new ResponseEntity<Erro>(erro, HttpStatus.NOT_FOUND);
	}
	
	@GetMapping(value="/{idCampeonato}/sortear")
	private ResponseEntity<?> sortearTimes(@PathVariable("idCampeonato") Integer idCampeonato) {
		Optional<Campeonato> campeonatoOptional = repositorio.findById(idCampeonato);
		Campeonato campeonato;
		
		if (campeonatoOptional.isPresent()) {
			campeonato = campeonatoOptional.get();
		}else {
			Erro erro = new Erro("Campeonato com id: "+idCampeonato+" não encontrado");
			logger.info(erro.getMensagem());
			return new ResponseEntity<Erro>(erro, HttpStatus.NOT_FOUND);
		}
		
		Integer qtdJogadores = campeonato.getJogadores().size();
		if (qtdJogadores < 4) {
			Erro erro = new Erro("Ao menos 4 jogadores devem ser inscritos para sortear os times");
			logger.info(erro.getMensagem());
			return new ResponseEntity<Erro>(erro, HttpStatus.BAD_REQUEST);
		}
		if (qtdJogadores > 40) {
			Erro erro = new Erro("Existem "+qtdJogadores+" jogadores inscritos no campeonato. Para sortear os times, o campeonato comporta no máximo 40 jogadores");
			logger.info(erro.getMensagem());
			return new ResponseEntity<Erro>(erro, HttpStatus.BAD_REQUEST);
		}
		if (qtdJogadores % 2 != 0) {
			Erro erro = new Erro("Existem "+qtdJogadores+" jogadores inscritos no campeonato. Para sortear os times, o número de jogadores inscritos deve ser par");
			logger.info(erro.getMensagem());
			return new ResponseEntity<Erro>(erro, HttpStatus.BAD_REQUEST);
		}
		
		
		BrasilTimesSerieA2018Enum[] qtdTimesEnum = BrasilTimesSerieA2018Enum.values();
		logger.info("Quantidade de times: "+qtdTimesEnum.length);
		
		
		// sorteia time
		// escolhe 2 jogadores para o time
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	private Integer sorteiaNumero(Integer min, Integer max) {
		Integer n = (max + 1 - min) + min;
		return (int) (Math.random() * n);
	}
	
}
