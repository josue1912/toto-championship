package br.com.toto.controller;

import br.com.toto.dto.Erro;
import br.com.toto.model.Partida;
import br.com.toto.repository.PartidaRepository;
import br.com.toto.utils.StatusPartidaEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(value = "/partidas")
public class PartidaController {

    @Autowired
    private PartidaRepository repositorio;

    private static final Logger logger = LoggerFactory.getLogger(PartidaController.class);

    @PutMapping(value = "/{idPartida}/incrementarPlacar/{idEquipe}")
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

    @PutMapping(value = "/{idPartida}/decrementarPlacar/{idEquipe}")
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
    public ResponseEntity<?> encerrarPartida(@PathVariable("idPartida") Integer idPartida){
        Optional<Partida> partidaOptional = repositorio.findById(idPartida);
        if (partidaOptional.isPresent()){
            Partida partida = partidaOptional.get();
            if(partida.getStatus().equals(StatusPartidaEnum.EM_ANDAMENTO)) {
                partida.setStatus(StatusPartidaEnum.ENCERRADA);
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
}
