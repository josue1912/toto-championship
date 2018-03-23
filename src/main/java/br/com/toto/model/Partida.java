package br.com.toto.model;

import br.com.toto.utils.StatusPartidaEnum;

import javax.persistence.*;

@Entity
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private Equipe timeA;

    @OneToOne
    private Equipe timeB;

    private Integer placarTimeA;

    private Integer placarTimeB;

    @Enumerated(value = EnumType.STRING)
    private StatusPartidaEnum status;

    @Deprecated
    public Partida(){}

    public Partida(Equipe timeA, Equipe timeB) {
        this.timeA = timeA;
        this.timeB = timeB;
        this.status = StatusPartidaEnum.NAO_REALIZADA;
        this.placarTimeA = 0;
        this.placarTimeB = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Equipe getTimeA() {
        return timeA;
    }

    public void setTimeA(Equipe timeA) {
        this.timeA = timeA;
    }

    public Equipe getTimeB() {
        return timeB;
    }

    public void setTimeB(Equipe timeB) {
        this.timeB = timeB;
    }

    public Integer getPlacarTimeA() {
        return placarTimeA;
    }

    public void incrementaPlacarTimeA() {
        this.placarTimeA++;
    }

    public void decrementaPlacarTimeA() {
        if (this.placarTimeA > 0) {
            this.placarTimeA--;
        }
    }

    public Integer getPlacarTimeB() {
        return placarTimeB;
    }

    public void incrementaPlacarTimeB() {
        this.placarTimeB++;
    }

    public void decrementaPlacarTimeB() {
        if (this.placarTimeB > 0) {
            this.placarTimeB--;
        }
    }

    public StatusPartidaEnum getStatus() {
        return status;
    }

    public void setStatus(StatusPartidaEnum status) {
        this.status = status;
    }
}
