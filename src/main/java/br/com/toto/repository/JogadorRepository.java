package br.com.toto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.toto.model.Jogador;

public interface JogadorRepository extends JpaRepository<Jogador, Integer>{

	public Optional<Jogador> findByEmail(String email);
}
