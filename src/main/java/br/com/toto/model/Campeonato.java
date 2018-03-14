package br.com.toto.model;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
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
	@Column(unique = true)
	private String nome;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@JsonFormat(pattern = "dd-MM-yyyy")
	@Column(name = "data_realizacao")
	@NotNull(message = "A data do campeonato deve ser informada")
	private Calendar dataRealizacao;

	@ManyToMany
	private Set<Jogador> jogadores;

	@ManyToMany
	private Set<Regra> regras;

	@Enumerated(EnumType.STRING)
	@NotNull
	private StatusCampeonato status;

	@ManyToMany
	private Set<Equipe> equipes;

	@OneToMany
	private Set<Partida> partidas;

	@Deprecated
	public Campeonato() {
	}

	public Campeonato(String nome, Calendar dataRealizacao) {
		this.nome = nome;
		this.dataRealizacao = dataRealizacao;
		this.equipes = new HashSet<>();
	}

	public Set<Partida> getPartidas() {
		return partidas;
	}

	public void setPartidas(Set<Partida> partidas) {
		this.partidas = partidas;
	}

	public Set<Equipe> getEquipes() {
		return equipes;
	}

	public void setEquipes(Set<Equipe> equipes) {
		this.equipes = equipes;
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
