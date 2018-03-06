package br.com.toto.model;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Campeonato {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull(message="O nome do campeonato deve ser informado")
	private String nome;
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern="dd-MM-yyyy")
	@NotNull
	private Calendar dataRealizacao;
	
	@OneToMany
	private Set<Time> times;
	
	@OneToMany
	private Set<Regra> regras;
	
	@Deprecated
	public Campeonato() {}
	
	public Campeonato(String nome, Calendar dataRealizacao) {
		this.nome = nome;
		this.dataRealizacao = dataRealizacao;
	}

	public Calendar getDataRealizacao() {
		return dataRealizacao;
	}

	public void setDataRealizacao(Calendar dataRealizacao) {
		this.dataRealizacao = dataRealizacao;
	}

	public Set<Regra> getRegras() {
		return regras;
	}

	public void setRegra(Set<Regra> regras) {
		this.regras = regras;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Set<Time> getTimes() {
		return times;
	}

	public void setTimes(Set<Time> times) {
		this.times = times;
	}
	
}
