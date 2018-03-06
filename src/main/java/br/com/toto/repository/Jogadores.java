package br.com.toto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.toto.model.Jogador;

public interface Jogadores extends JpaRepository<Jogador, Integer>{

}
