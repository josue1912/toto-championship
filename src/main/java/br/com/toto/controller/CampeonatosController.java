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
import br.com.toto.repository.CampeonatoRepository;

@RestController
@RequestMapping("/campeonatos")
public class CampeonatosController {

	@Autowired
	private CampeonatoRepository repositorio;
	
	public static final Logger logger = LoggerFactory.getLogger(CampeonatosController.class);
	
	@PostMapping
	public ResponseEntity<?> criarCampeonato(@RequestBody Campeonato campeonato, UriComponentsBuilder ucBuilder ){
		logger.info("Cadastrando campeonato {}", campeonato);
		Campeonato campeonatoSalvo = repositorio.save(campeonato);
		logger.info("Campeonato cadastrado com sucesso.");
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/campeonatos/{id}").buildAndExpand(campeonatoSalvo.getId()).toUri());
		return new ResponseEntity<Campeonato>(campeonatoSalvo, headers, HttpStatus.CREATED);				
	}
	
	@GetMapping
	public ResponseEntity<?> listarCampeonatos() {
		List<Campeonato> campeonatos = repositorio.findAll();
		if (campeonatos.size() > 0) {
			return new ResponseEntity<List<Campeonato>>(campeonatos, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<?> buscaCampeonatoPorId(@PathVariable("id") Integer id) {
		logger.info("Buscando pelo campeonato {}", id);
		Optional<Campeonato> campeonato = repositorio.findById(id);
		if (campeonato.isPresent()) {
			return new ResponseEntity<Campeonato>(campeonato.get(), HttpStatus.OK);	
		}else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	
}
