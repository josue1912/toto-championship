package br.com.toto.controller;

import br.com.toto.dto.Erro;
import br.com.toto.model.*;
import br.com.toto.repository.*;
import br.com.toto.utils.StatusCampeonatoEnum;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Stream;

@RestController
@RequestMapping
        (value = "/campeonatos", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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


    @PostMapping(value = "/{idCampeonato}/jogador")
	@ApiOperation(
            value = "Cadastra e/ou inscreve um jogador no campeonato",
            response = Campeonato.class,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<?> inscreverJogadorNoCampeonato(@PathVariable("idCampeonato") Integer idCampeonato,
                                                          @Valid @RequestBody(required = true) Jogador jogador) {
        Optional<Campeonato> campeonatoOptional = repositorio.findById(idCampeonato);
        Campeonato campeonato;
        if (campeonatoOptional.isPresent()) {
            campeonato = campeonatoOptional.get();
            if (campeonato.getStatus().equals(StatusCampeonatoEnum.EM_ANDAMENTO) ||
                    campeonato.getStatus().equals(StatusCampeonatoEnum.ENCERRADO)) {
                Erro erro = new Erro("O campeonato com id [" + idCampeonato + "] não pode ser alterado pois está com status ["+campeonato.getStatus()+"]");
                logger.info(erro.getMensagem());
                return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
            }else {
                Optional<Jogador> jogadorOptional = repositorioJogador.findByEmail(jogador.getEmail());
                if (jogadorOptional.isPresent()) {
                    campeonato.getJogadores().add(jogadorOptional.get());
                    logger.info("O jogador [{}] já existia e foi inscrito no campeonato [{}]", jogador.getNome(),
                            campeonato.getNome());
                } else {
                    Jogador novoJogador = repositorioJogador.save(jogador);
                    campeonato.getJogadores().add(novoJogador);
                    logger.info("O jogador [{}] foi cadastrado e inscrito no campeonato [{}]", jogador.getNome(), campeonato.getNome());
                }
                campeonato.setStatus(StatusCampeonatoEnum.EM_CRIACAO);
                repositorio.save(campeonato);
            }
        } else {
            Erro erro = new Erro("Campeonato com id [" + idCampeonato + "] não encontrado");
            logger.info(erro.getMensagem());
            return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(campeonato, HttpStatus.CREATED);
    }

	@PostMapping
	@ApiOperation(
                value = "Cadastrar campeonato",
                response = Campeonato.class,
                consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
			)
	public ResponseEntity<?> criarCampeonato(@Valid @RequestBody Campeonato campeonato, UriComponentsBuilder ucBuilder) {
		logger.info("Objeto recebido"+campeonato.getNome()+" - "+campeonato.getDataRealizacao());

		Optional<Campeonato> campeonatoOptional = repositorio.findByNome(campeonato.getNome());
		if (campeonatoOptional.isPresent()) {
			Erro erro = new Erro("Já existe um campeonato com este nome");
			logger.info(erro.getMensagem());
			return new ResponseEntity<>(erro, HttpStatus.CONFLICT);
		}
		Campeonato campeonatoSalvo = repositorio.save(campeonato);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/campeonatos/{id}").buildAndExpand(campeonatoSalvo.getId()).toUri());
		logger.info("Campeonato cadastrado com sucesso!");
		return new ResponseEntity<>(campeonatoSalvo, headers, HttpStatus.CREATED);
	}

	@GetMapping
    @ApiOperation(
            value = "Listar todos os campeonatos cadastrados",
            response = Campeonato[].class
    )
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
    @ApiOperation(
            value = "Buscar um campeonato por {id}",
            response = Campeonato.class
    )
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

	@PutMapping(value="/{idCampeonato}/iniciar")
    @ApiOperation(
            value = "Inicia um campeonato",
            response = Campeonato.class,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
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

	@PutMapping(value = "/{idCampeonato}/jogador/{idJogador}")
    @ApiOperation(
            value = "Remove um jogador do campeonato",
            response = Campeonato.class,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
	public ResponseEntity<?> removerJogadorDoCampeonato(@PathVariable("idCampeonato") Integer idCampeonato,
			@PathVariable("idJogador") Integer idJogador) {
		Optional<Campeonato> campeonatoOptional = repositorio.findById(idCampeonato);
		if (campeonatoOptional.isPresent()) {
            Campeonato campeonato = campeonatoOptional.get();
            if (campeonato.getStatus().equals(StatusCampeonatoEnum.EM_ANDAMENTO) ||
                    campeonato.getStatus().equals(StatusCampeonatoEnum.ENCERRADO)){
                Erro erro = new Erro("O campeonato com id ["+idCampeonato+"] não pode ser alterado pois está com status ["+campeonato.getStatus()+"]");
                logger.info(erro.getMensagem());
                return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
            }else{
                Optional<Jogador> jogadorOptional = repositorioJogador.findById(idJogador);
                if (jogadorOptional.isPresent()) {
                    campeonato.getJogadores().remove(jogadorOptional.get());
                    repositorio.save(campeonato);
                    return new ResponseEntity<>(campeonato, HttpStatus.OK);
                } else {
                    Erro erro = new Erro("Jogador com id [" + idJogador + "] não encontrado");
                    logger.info(erro.getMensagem());
                    return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
                }
            }

		}
		Erro erro = new Erro("Campeonato com id [" + idCampeonato + "] não encontrado");
		logger.info(erro.getMensagem());
		return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
	}

	@PutMapping(value = "/{idCampeonato}/sortearEquipes")
    @ApiOperation(
            value = "Sorteia as equipes para a disputa do campeonato",
            response = Campeonato.class,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
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
		campeonato.setStatus(StatusCampeonatoEnum.EM_ANDAMENTO);
		repositorio.save(campeonato);
		return new ResponseEntity<>(campeonato, HttpStatus.OK);
	}

	private void montarEquipes(Campeonato campeonato) {
        logger.info("Montando equipes...");
		Set<Time> times = sortearTimes(campeonato);
		Equipe equipe;
		for (Time time : times) {
            logger.info("Time [{}]",time.getNome());
		    equipe = new Equipe(time.getNome());
			Set<Jogador> jogadores = new HashSet<>();
			jogadores.add(sortearJogador(campeonato));
			jogadores.add(sortearJogador(campeonato));
			equipe.setJogadores(jogadores);
			repositorioEquipe.save(equipe);
			campeonato.getEquipes().add(equipe);
		}
	}

	private Jogador sortearJogador(Campeonato campeonato) {
		Integer jogadorSorteado = new Random().nextInt(campeonato.getJogadores().size());
		Jogador jogador = (Jogador) campeonato.getJogadores().toArray()[jogadorSorteado];
		campeonato.getJogadores().remove(jogador);
		logger.info("Jogador sorteado [{}]",jogador.getNome());
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

	@GetMapping(value = "/{idCampeonato}/classificacao")
    public ResponseEntity<?> retornaClassificacaoDoCampeonato(@PathVariable(value = "idCampeonato") Integer idCampeonato){
        Optional<Campeonato> campeonatoOptional = repositorio.findById(idCampeonato);
        if (campeonatoOptional.isPresent()){
            if (campeonatoOptional.get().getStatus().equals(StatusCampeonatoEnum.EM_ANDAMENTO)){
                Stream<Equipe> classificacao = campeonatoOptional.get().getEquipes().stream()
                        .sorted(Comparator.reverseOrder());
                return new ResponseEntity<>(classificacao, HttpStatus.OK);
            }else{
                Erro erro = new Erro("Campeonato com id [" + idCampeonato+ "] não está em andamento");
                logger.info(erro.getMensagem());
                return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
            }
        }else{
            Erro erro = new Erro("Campeonato com id [" + idCampeonato+ "] não encontrado");
            logger.info(erro.getMensagem());
            return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
        }
    }

}
