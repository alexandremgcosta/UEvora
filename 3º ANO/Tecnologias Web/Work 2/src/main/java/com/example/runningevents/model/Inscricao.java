package com.example.runningevents.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "inscricao")
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "eventoId", referencedColumnName = "id")
    private Evento evento;

    @ManyToOne
    @JoinColumn(name = "participanteId", referencedColumnName = "id")
    private Utilizador participante;

    private Timestamp tempoStart;
    private Timestamp tempoP1;
    private Timestamp tempoP2;
    private Timestamp tempoP3;
    private Timestamp tempoFinish;
    private String genero;
    private String escalao;
    private boolean pago;

    public Inscricao() {
    }

    public Inscricao(Evento evento, Utilizador participante, Timestamp tempoStart, Timestamp tempoP1, Timestamp tempoP2, Timestamp tempoP3, Timestamp tempoFinish, String genero, String escalao) {
        this.evento = evento;
        this.participante = participante;
        this.tempoStart = tempoStart;
        this.tempoP1 = tempoP1;
        this.tempoP2 = tempoP2;
        this.tempoP3 = tempoP3;
        this.tempoFinish = tempoFinish;
        this.genero = genero;
        this.escalao = escalao;
        this.pago=false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public Utilizador getParticipante() {
        return participante;
    }

    public void setParticipante(Utilizador participante) {
        this.participante = participante;
    }

    public Timestamp getTempoStart() {
        return tempoStart;
    }

    public void setTempoStart(Timestamp tempoStart) {
        this.tempoStart = tempoStart;
    }

    public Timestamp getTempoP1() {
        return tempoP1;
    }

    public void setTempoP1(Timestamp tempoP1) {
        this.tempoP1 = tempoP1;
    }

    public Timestamp getTempoP2() {
        return tempoP2;
    }

    public void setTempoP2(Timestamp tempoP2) {
        this.tempoP2 = tempoP2;
    }

    public Timestamp getTempoP3() {
        return tempoP3;
    }

    public void setTempoP3(Timestamp tempoP3) {
        this.tempoP3 = tempoP3;
    }

    public Timestamp getTempoFinish() {
        return tempoFinish;
    }

    public void setTempoFinish(Timestamp tempoFinish) {
        this.tempoFinish = tempoFinish;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEscalao() {
        return escalao;
    }

    public void setEscalao(String escalao) {
        this.escalao = escalao;
    }
}
