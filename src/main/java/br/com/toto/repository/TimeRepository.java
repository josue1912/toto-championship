package br.com.toto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.toto.model.Time;

public interface TimeRepository extends JpaRepository<Time, Integer>{

}
