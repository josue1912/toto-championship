package br.com.toto.controller;

import br.com.toto.dto.Erro;
import br.com.toto.model.Campeonato;
import br.com.toto.model.Equipe;
import br.com.toto.model.Partida;
import br.com.toto.repository.CampeonatoRepository;
import br.com.toto.repository.EquipeRepository;
import br.com.toto.repository.PartidaRepository;
import br.com.toto.utils.StatusCampeonatoEnum;
import br.com.toto.utils.StatusPartidaEnum;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/partidas", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PartidaController {

    @Autowired
    private PartidaRepository repositorio;

    @Autowired
    private CampeonatoRepository repositorioCampeonato;

    @Autowired
    private EquipeRepository equipeRepository;

    private static final Logger logger = LoggerFactory.getLogger(PartidaController.class);

    @PutMapping(value = "/{idPartida}/incrementarPlacar/{idEquipe}")
    @ApiOperation(
            value = "Incrementa um gol ao placar da equipe",
            response = Partida.class
    )
    public ResponseEntity<?> incrementarPlacar(@PathVariable("idPartida") Integer idPartida, @PathVariable("idEquipe") Integer idEquipe){
        Optional<Partida> partidaOptional = repositorio.findById(idPartida);
        if (partidaOptional.isPresent()){
            Partida partida = partidaOptional.get();
            if (partida.getStatus().equals(StatusPartidaEnum.EM_ANDAMENTO)) {
                if (partida.getTimeA().getId() == idEquipe) {
                    partida.incrementaPlacarTimeA();
                } else if (partida.getTimeB().getId() == idEquipe) {
                    partida.incrementaPlacarTimeB();
                } else {
                    Erro erro = new Erro("Equipe com id [" + idEquipe + "] inválida para esta partida");
                    logger.info(erro.getMensagem());
                    return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
                }
                repositorio.save(partida);
                return new ResponseEntity<>(partida, HttpStatus.OK);
            }else{
                Erro erro = new Erro("Esta partida não está em andamento");
                logger.info(erro.getMensagem());
                return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
            }
        }
        Erro erro = new Erro("Partida com id ["+idPartida+"] não encontrada");
        logger.info(erro.getMensagem());
        return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
    }

    @PutMapping(value = "/{idPartida}/decrementarPlacar/{idEquipe}")
    @ApiOperation(
            value = "Decrementa um gol ao placar da equipe",
            response = Partida.class,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<?> decrementarPlacar(@PathVariable("idPartida") Integer idPartida, @PathVariable("idEquipe") Integer idEquipe){
        Optional<Partida> partidaOptional = repositorio.findById(idPartida);
        if (partidaOptional.isPresent()){
            Partida partida = partidaOptional.get();
            if (partida.getStatus().equals(StatusPartidaEnum.EM_ANDAMENTO)) {
                if (partida.getTimeA().getId() == idEquipe) {
                    partida.decrementaPlacarTimeA();
                } else if (partida.getTimeB().getId() == idEquipe) {
                    partida.decrementaPlacarTimeB();
                } else {
                    Erro erro = new Erro("Equipe com id [" + idPartida + "] inválida para esta partida");
                    logger.info(erro.getMensagem());
                    return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
                }
                repositorio.save(partida);
                return new ResponseEntity<>(partida, HttpStatus.OK);
            }else{
                Erro erro = new Erro("Esta partida não está em andamento");
                logger.info(erro.getMensagem());
                return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
            }
        }
        Erro erro = new Erro("Partida com id ["+idPartida+"] não encontrada");
        logger.info(erro.getMensagem());
        return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
    }

    @PutMapping(value="/{idPartida}/encerrarPartida")
    @ApiOperation(
            value = "Encerra uma partida do campeonato",
            response = Partida.class,
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<?> encerrarPartida(@PathVariable("idPartida") Integer idPartida){
        Optional<Partida> partidaOptional = repositorio.findById(idPartida);
        if (partidaOptional.isPresent()){
            Partida partida = partidaOptional.get();
            if(partida.getStatus().equals(StatusPartidaEnum.EM_ANDAMENTO)) {
                partida.setStatus(StatusPartidaEnum.ENCERRADA);
                repositorio.save(partida);
                this.atualizaClassificacao(partida);
                return new ResponseEntity<>(partida, HttpStatus.OK);
            }else{
                Erro erro = new Erro("Esta partida não está em andamento");
                logger.info(erro.getMensagem());
                return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
            }
        }
        Erro erro = new Erro("Partida com id ["+idPartida+"] não encontrada");
        logger.info(erro.getMensagem());
        return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/campeonato/{idCampeonato}/proximaPartida")
    @ApiOperation(
            value = "Sorteia a próxima partida do campeonato",
            response = Partida.class
    )
    private ResponseEntity<?> proximaPartida(@PathVariable("idCampeonato") Integer idCampeonato){
        Optional<Campeonato> campeonatoOptional = repositorioCampeonato.findById(idCampeonato);
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
                    repositorio.save(partida);
                    logger.info("Proxima partida [{} x {}]",partida.getTimeA().getNome(), partida.getTimeB().getNome());
                    return new ResponseEntity<>(partida, HttpStatus.OK);
                }else{
                    campeonato.setStatus(StatusCampeonatoEnum.ENCERRADO);
                    repositorioCampeonato.save(campeonato);
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

    private void atualizaClassificacao(Partida partida){
        Equipe equipeA = equipeRepository.findById(partida.getTimeA().getId()).get();
        Equipe equipeB = equipeRepository.findById(partida.getTimeB().getId()).get();

        if (partida.getPlacarTimeA() == partida.getPlacarTimeB()){
            equipeA.adicionaEmpate(partida.getPlacarTimeA());
            equipeB.adicionaEmpate(partida.getPlacarTimeB());
        }else if (partida.getPlacarTimeA() > partida.getPlacarTimeB()){
            equipeA.adicionaVitoria(partida.getPlacarTimeA(),partida.getPlacarTimeB());
            equipeB.adicionaDerrota(partida.getPlacarTimeB(),partida.getPlacarTimeA());
        }else if (partida.getPlacarTimeA() < partida.getPlacarTimeB()){
            equipeA.adicionaDerrota(partida.getPlacarTimeA(),partida.getPlacarTimeB());
            equipeB.adicionaVitoria(partida.getPlacarTimeB(),partida.getPlacarTimeA());
        }

        equipeRepository.save(equipeA);
        equipeRepository.save(equipeB);
    }
}
