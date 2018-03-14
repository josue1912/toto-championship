package br.com.toto.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Equipe {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String nome;

	@ManyToMany
	private Set<Jogador> jogadores;

	@Deprecated
	public Equipe() {
	}

	public Equipe(String nome) {
		this.nome = nome;
		this.jogadores = new HashSet<>();
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
