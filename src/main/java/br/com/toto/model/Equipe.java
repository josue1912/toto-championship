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

    private Integer pontos;
    private Integer vitorias;
    private Integer derrotas;
    private Integer empates;
    private Integer golsAFavor;
    private Integer golsContra;
    private Integer saldoDeGols;

    @Deprecated
    public Equipe() {
    }

    public Equipe(String nome) {
        this.nome = nome;
        this.jogadores = new HashSet<>();
        this.pontos = 0;
        this.vitorias = 0;
        this.derrotas = 0;
        this.empates = 0;
        this.golsAFavor = 0;
        this.golsContra = 0;
        this.saldoDeGols = 0;
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

    public Integer getSaldoDeGols() {
        return golsAFavor-golsContra;
    }

    public Integer getPontos() {
        return pontos;
    }

    private void adicionaPontos(Integer pontos) {
        this.pontos += pontos;
    }

    public Integer getVitorias() {
        return vitorias;
    }

    public void adicionaVitoria(Integer GolsAFavor, Integer GolsContra) {
        this.vitorias++;
        this.adicionaPontos(3);
        this.adicionaGolsAFavor(GolsAFavor);
        this.adicionaGolsContra(GolsContra);
    }

    public Integer getDerrotas() {
        return derrotas;
    }

    public void adicionaDerrota(Integer GolsAFavor, Integer GolsContra) {
        this.derrotas++;
        this.adicionaGolsAFavor(GolsAFavor);
        this.adicionaGolsContra(GolsContra);
    }

    public Integer getEmpates() {
        return empates;
    }

    public void adicionaEmpate(Integer gols) {
        this.empates++;
        this.adicionaPontos(1);
        this.adicionaGolsAFavor(gols);
        this.adicionaGolsContra(gols);
    }

    public Integer getGolsAFavor() {
        return golsAFavor;
    }

    private void adicionaGolsAFavor(Integer golsAFavor) {
        this.golsAFavor += golsAFavor;
    }

    public Integer getGolsContra() {
        return golsContra;
    }

    private void adicionaGolsContra(Integer golsContra) {
        this.golsContra += golsContra;
    }
}
