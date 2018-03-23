package br.com.toto.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

import br.com.toto.utils.StatusPartidaEnum;
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
import br.com.toto.model.Campeonato;
import br.com.toto.model.Equipe;
import br.com.toto.model.Jogador;
import br.com.toto.model.Partida;
import br.com.toto.model.Time;
import br.com.toto.repository.CampeonatoRepository;
import br.com.toto.repository.EquipeRepository;
import br.com.toto.repository.JogadorRepository;
import br.com.toto.repository.PartidaRepository;
import br.com.toto.repository.TimeRepository;
import br.com.toto.utils.StatusCampeonatoEnum;

@RestController
@RequestMapping("/campeonatos")
public class CampeonatoController {

	@Autowired
	private CampeonatoRepository repositorio;

	@Autowired
	private JogadorRepository repositorioJogador;

	@Autowired
	private TimeRepository repositorioTime;

	@Autowired
	private EquipeRepository repositorioEquipe;

	@Autowired
	private PartidaRepository repositorioPartida;

	private static final Logger logger = LoggerFactory.getLogger(CampeonatoController.class);

	@PostMapping
	public ResponseEntity<?> criarCampeonato(@RequestBody Campeonato campeonato, UriComponentsBuilder ucBuilder) {
		Optional<Campeonato> campeonatoOptional = repositorio.findByNome(campeonato.getNome());
		if (campeonatoOptional.isPresent()) {
			Erro erro = new Erro("Já existe um campeonato com este nome");
			logger.info(erro.getMensagem());
			return new ResponseEntity<>(erro, HttpStatus.CONFLICT);
		}
		campeonato.setStatus(StatusCampeonatoEnum.EM_CRIACAO);
		Campeonato campeonatoSalvo = repositorio.save(campeonato);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/campeonatos/{id}").buildAndExpand(campeonatoSalvo.getId()).toUri());
		logger.info("Campeonato cadastrado com sucesso!");
		return new ResponseEntity<>(campeonatoSalvo, headers, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<?> listarCampeonatos() {
		List<Campeonato> campeonatos = repositorio.findAll();
		if (campeonatos.size() > 0) {
			logger.info("{} campeonatos encontrados!", campeonatos.size());
			return new ResponseEntity<>(campeonatos, HttpStatus.OK);
		}
		Erro erro = new Erro("Nenhum campeonato encontrado");
		logger.info(erro.getMensagem());
		return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> buscarCampeonatoPorId(@PathVariable("id") Integer id) {
		Optional<Campeonato> campeonato = repositorio.findById(id);
		if (campeonato.isPresent()) {
			logger.info("Campeonato com id [{}] encontrado", id);
			return new ResponseEntity<>(campeonato.get(), HttpStatus.OK);
		}
		Erro erro = new Erro("Campeonato com id [" + id + "] não encontrado");
		logger.info(erro.getMensagem());
		return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
	}

	@PostMapping(value = "/{idCampeonato}/jogador")
	public ResponseEntity<?> inscreverJogadorNoCampeonato(@PathVariable("idCampeonato") Integer idCampeonato,
			@RequestBody(required = true) Jogador jogador) {
		Optional<Campeonato> campeonatoOptional = repositorio.findById(idCampeonato);
		Campeonato campeonato;
		if (campeonatoOptional.isPresent()) {
			campeonato = campeonatoOptional.get();
			Optional<Jogador> jogadorOptional = repositorioJogador.findByEmail(jogador.getEmail());
			Jogador novoJogador;
			if (jogadorOptional.isPresent()) {
				campeonato.getJogadores().add(jogadorOptional.get());
				logger.info("O jogador [{}] já existia e foi inscrito no campeonato [{}]", jogador.getNome(),
						campeonato.getNome());
			} else {
				novoJogador = repositorioJogador.save(jogador);
				campeonato.getJogadores().add(novoJogador);
				logger.info("O jogador [{}] foi inscrito no campeonato [{}]", jogador.getNome(), campeonato.getNome());
			}
			campeonato.setStatus(StatusCampeonatoEnum.EM_CRIACAO);
			repositorio.save(campeonato);
		} else {
			Erro erro = new Erro("Campeonato com id [" + idCampeonato + "] não encontrado");
			logger.info(erro.getMensagem());
			return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(campeonato, HttpStatus.CREATED);
	}

	@PutMapping(value="/{idCampeonato}/iniciar")
	public ResponseEntity<?> iniciarCampeonato(@PathVariable("idCampeonato") Integer idCampeonato){
		Optional<Campeonato> campeonatoOptional = repositorio.findById(idCampeonato);
		if (campeonatoOptional.isPresent()) {
			Campeonato campeonato = campeonatoOptional.get();
            if (campeonato.getStatus().equals(StatusCampeonatoEnum.ENCERRADO)) {
                Erro erro = new Erro("O campeonato ["+campeonato.getNome()+"] está encerrado");
                logger.info(erro.getMensagem());
                return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
            } else if (campeonato.getStatus().equals(StatusCampeonatoEnum.CONFIGURADO)){
				campeonato.setStatus(StatusCampeonatoEnum.EM_ANDAMENTO);
				logger.info("Campeonato [{}] iniciado",campeonato.getNome());
				return new ResponseEntity<>(campeonato, HttpStatus.OK);
			} else {
				Erro erro = new Erro("O campeonato ["+campeonato.getNome()+"] não está configurado. Sorteie as equipes antes de iniciar o campeonato");
				logger.info(erro.getMensagem());
				return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
			}
		}
		Erro erro = new Erro("Campeonato com id [" + idCampeonato + "] não encontrado");
		logger.info(erro.getMensagem());
		return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
	}

	@GetMapping(value = "/{idCampeonato}/proximaPartida")
	private ResponseEntity<?> proximaPartida(@PathVariable("idCampeonato") Integer idCampeonato){
	    Optional<Campeonato> campeonatoOptional = repositorio.findById(idCampeonato);
        if (campeonatoOptional.isPresent()){
            Campeonato campeonato = campeonatoOptional.get();
            if (campeonato.getStatus().equals(StatusCampeonatoEnum.CONFIGURADO)){
                Optional<Partida> partidaEmAndamentoOptional = campeonato.getPartidas().stream().filter(p -> p.getStatus().equals(StatusPartidaEnum.EM_ANDAMENTO)).findFirst();
                if (partidaEmAndamentoOptional.isPresent()){
                    Partida partidaEmAndamento = partidaEmAndamentoOptional.get();
                    Erro erro = new Erro("A partida ["+ partidaEmAndamento.getTimeA().getNome()+" x "+partidaEmAndamento.getTimeB().getNome()+"] está em andamento. Encerre a partida antes de buscar a próxima partida");
                    logger.info(erro.getMensagem());
                    return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
                }

                Optional<Partida> partidaOptional = campeonato.getPartidas().stream().filter(p -> p.getStatus().equals(StatusPartidaEnum.NAO_REALIZADA)).findAny();
                if (partidaOptional.isPresent()) {
                    Partida partida = partidaOptional.get();
                    partida.setStatus(StatusPartidaEnum.EM_ANDAMENTO);
                    repositorioPartida.save(partida);
                    logger.info("Proxima partida [{} x {}]",partida.getTimeA().getNome(), partida.getTimeB().getNome());
                    return new ResponseEntity<>(partida, HttpStatus.OK);
                }else{
                    campeonato.setStatus(StatusCampeonatoEnum.ENCERRADO);
                    repositorio.save(campeonato);
                    return new ResponseEntity<>(campeonato, HttpStatus.OK);
                }
            }else if (campeonato.getStatus().equals(StatusCampeonatoEnum.ENCERRADO)) {
                Erro erro = new Erro("O campeonato  ["+ campeonato.getNome()+"] está encerrado");
                logger.info(erro.getMensagem());
                return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
            }else{
                Erro erro = new Erro("O campeonato  ["+ campeonato.getNome()+"] não está totalmente configurado");
                logger.info(erro.getMensagem());
                return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
            }
        }
        Erro erro = new Erro("Nenhuma partida com o status [NAO_REALIZADA] foi encontrada");
        logger.info(erro.getMensagem());
        return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
    }

	@PutMapping(value = "/{idCampeonato}/jogador/{idJogador}")
	public ResponseEntity<?> removerJogadorDoCampeonato(@PathVariable("idCampeonato") Integer idCampeonato,
			@PathVariable("idJogador") Integer idJogador) {
		Optional<Campeonato> campeonatoOptional = repositorio.findById(idCampeonato);
		if (campeonatoOptional.isPresent()) {
			Optional<Jogador> jogadorOptional = repositorioJogador.findById(idJogador);
			if (jogadorOptional.isPresent()) {
				Campeonato campeonato = campeonatoOptional.get();
				campeonato.getJogadores().remove(jogadorOptional.get());
				repositorio.save(campeonato);
				return new ResponseEntity<>(campeonato, HttpStatus.OK);
			} else {
				Erro erro = new Erro("Jogador com id [" + idJogador + "] não encontrado");
				logger.info(erro.getMensagem());
				return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
			}
		}
		Erro erro = new Erro("Campeonato com id [" + idCampeonato + "] não encontrado");
		logger.info(erro.getMensagem());
		return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
	}

	@PutMapping(value = "/{idCampeonato}/sortearEquipes")
	private ResponseEntity<?> sortearEquipes(@PathVariable("idCampeonato") Integer idCampeonato) {
		Optional<Campeonato> campeonatoOptional = repositorio.findById(idCampeonato);
		Campeonato campeonato;

		if (campeonatoOptional.isPresent()) {
			campeonato = campeonatoOptional.get();
		} else {
			Erro erro = new Erro("Campeonato com id [" + idCampeonato + "] não encontrado");
			logger.info(erro.getMensagem());
			return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
		}

		if (campeonato.getStatus() != StatusCampeonatoEnum.EM_CRIACAO) {
			Erro erro = new Erro("O campeonato ["+campeonato.getNome()+"] não pode mais ser alterado");
			logger.info(erro.getMensagem());
			return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
		}

		Integer qtdJogadores = campeonato.getJogadores().size();
		if ((qtdJogadores < 4 || qtdJogadores > 40) && campeonato.getEquipes().size() < 1) {
			Erro erro = new Erro("Existem [" + qtdJogadores
					+ "] jogadores inscritos no campeonato ["+campeonato.getNome()+"]. Para sortear os times, o campeonato deve ter entre 4 e 40 jogadores");
			logger.info(erro.getMensagem());
			return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
		}
		if (qtdJogadores % 2 != 0) {
			Erro erro = new Erro("Existem [" + qtdJogadores
					+ "] jogadores inscritos no campeonato ["+campeonato.getNome()+"]. Para sortear os times, o número de jogadores inscritos deve ser par");
			logger.info(erro.getMensagem());
			return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
		}

		montarEquipes(campeonato);
		montarTabelaDePartidas(campeonato);
		repositorio.save(campeonato);
		return new ResponseEntity<>(campeonato, HttpStatus.OK);
	}

	private void montarEquipes(Campeonato campeonato) {
		Set<Time> times = sortearTimes(campeonato);
		Equipe equipe;
		for (Time time : times) {
			equipe = new Equipe(time.getNome());
			Set<Jogador> jogadores = new HashSet<>();
			jogadores.add(sortearJogador(campeonato));
			jogadores.add(sortearJogador(campeonato));
			equipe.setJogadores(jogadores);
			repositorioEquipe.save(equipe);
			campeonato.getEquipes().add(equipe);
		}
		campeonato.setStatus(StatusCampeonatoEnum.CONFIGURADO);
	}

	private Jogador sortearJogador(Campeonato campeonato) {
		Integer jogadorSorteado = new Random().nextInt(campeonato.getJogadores().size());
		Jogador jogador = (Jogador) campeonato.getJogadores().toArray()[jogadorSorteado];
		campeonato.getJogadores().remove(jogador);
		return jogador;
	}

	private Set<Time> sortearTimes(Campeonato campeonato) {
		List<Time> timesList = repositorioTime.findAll();
		Set<Time> timesSet = new HashSet<>();
		Integer qtdTimes = campeonato.getJogadores().size() / 2;
		while (timesSet.size() != qtdTimes) {
			Time time = timesList.get(new Random().nextInt(timesList.size()));
			logger.info("Time sorteado [{}]", time.getNome());
			timesSet.add(time);
		}
		return timesSet;
	}

	private void montarTabelaDePartidas(Campeonato campeonato){
		Object[] equipes = campeonato.getEquipes().toArray();

		for (int i=0; i<equipes.length; i++){
			Equipe timeA = (Equipe) equipes[i];
			for (int j=i+1; j<equipes.length; j++){
				Equipe timeB = (Equipe) equipes[j];
				Partida partida = new Partida(timeA, timeB);
				logger.info("Partida [{} x {}]",timeA.getNome(), timeB.getNome());
				repositorioPartida.save(partida);
				campeonato.getPartidas().add(partida);
			}
		}
	}
}
