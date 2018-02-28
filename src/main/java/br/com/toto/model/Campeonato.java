package br.com.toto.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Campeonato {

	@Id
	@GeneratedValue
	private Long id;
	private String nome;
	private LocalDateTime dataRealizacao;
	@ManyToMany
	private List<Time> times;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDateTime getDataJogo() {
		return dataRealizacao;
	}

	public void setDataJogo(LocalDateTime dataRealizacao) {
		this.dataRealizacao = dataRealizacao;
	}

	public List<Time> getTimes() {
		return times;
	}

	public void setTimes(List<Time> times) {
		this.times = times;
	}

}
