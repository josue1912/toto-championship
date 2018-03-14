package br.com.toto.repository;

import br.com.toto.model.Partida;
import br.com.toto.model.Time;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartidaRepository extends JpaRepository<Partida, Integer>{

}
