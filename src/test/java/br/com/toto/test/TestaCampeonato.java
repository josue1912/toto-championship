package br.com.toto.test;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.toto.model.Campeonato;
import br.com.toto.model.Regra;
import br.com.toto.model.Time;
import br.com.toto.repository.CampeonatoRepository;


public class TestaCampeonato {
	
	@Autowired
	private static CampeonatoRepository repoCampeonatos;
	
	
	public static void main(String[] args) {
		Set<Regra> regras = new HashSet<>();
		regras.add(new Regra("Roletou é penalti"));
		regras.add(new Regra("O jogo terá dois tempos de 2 minutos com troca de lado"));
		
		Set<Time> times = new HashSet<>();
		times.add(new Time("Flamengo"));
		times.add(new Time("River Plate"));
		
		Campeonato camp = new Campeonato("Brasileirao 2018",Calendar.getInstance());
		camp.setRegra(regras);
		camp.setTimes(times);
		
		repoCampeonatos.save(camp);		
		
	}
	

}
