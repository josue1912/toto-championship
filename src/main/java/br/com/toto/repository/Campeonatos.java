package br.com.toto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.toto.model.Campeonato;

public interface Campeonatos extends JpaRepository<Campeonato, Long> {

}
