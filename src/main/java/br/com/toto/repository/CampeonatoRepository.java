package br.com.toto.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.toto.model.Campeonato;

public interface CampeonatoRepository extends JpaRepository<Campeonato, Integer> {

	public Optional<Campeonato> findByNome(String nome);
}
