package br.com.toto.model;

import br.com.toto.utils.StatusPartida;

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
    private StatusPartida status;

    @Deprecated
    public Partida(){}

    public Partida(Equipe timeA, Equipe timeB) {
        this.timeA = timeA;
        this.timeB = timeB;
        this.status = StatusPartida.NAO_REALIZADA;
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

    public void setPlacarTimeA(Integer placarTimeA) {
        this.placarTimeA = placarTimeA;
    }

    public Integer getPlacarTimeB() {
        return placarTimeB;
    }

    public void setPlacarTimeB(Integer placarTimeB) {
        this.placarTimeB = placarTimeB;
    }

    public StatusPartida getStatus() {
        return status;
    }

    public void setStatus(StatusPartida status) {
        this.status = status;
    }
}
