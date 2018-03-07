package br.com.toto.model;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.toto.utils.StatusCampeonato;

@Entity
public class Campeonato {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull(message = "O nome do campeonato deve ser informado")
	@Column(unique=true)
	private String nome;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	@Column(name="data_realizacao")
	@NotNull(message = "A data do campeonato deve ser informada")
	private Calendar dataRealizacao;
	
	@OneToMany
	private Set<Jogador> jogadores;

	@OneToMany
	private Set<Regra> regras;

	@Enumerated
	@NotNull
	private StatusCampeonato status;

	@Deprecated
	public Campeonato() {
	}

	public Campeonato(String nome, Calendar dataRealizacao) {
		this.nome = nome;
		this.dataRealizacao = dataRealizacao;
	}

	public StatusCampeonato getStatus() {
		return status;
	}

	public void setStatus(StatusCampeonato status) {
		this.status = status;
	}

	public void setRegras(Set<Regra> regras) {
		this.regras = regras;
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

	public Set<Jogador> getJogadores() {
		return jogadores;
	}

	public void setJogadores(Set<Jogador> jogadores) {
		this.jogadores = jogadores;
	}


}
