package com.example.SeekArtist.model.donativo;

import com.example.SeekArtist.model.artista.Artista;
import com.example.SeekArtist.model.utilizador.Utilizador;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Donativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "artista_id")
    private Artista artista;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "utulizador_id")
    private Utilizador utilizador;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDateTime data;

    public Donativo(Artista artista, Utilizador utilizador, BigDecimal valor, LocalDateTime data) {
        this.artista = artista;
        this.utilizador = utilizador;
        this.valor = valor;
        this.data = data;
    }

    public Donativo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Artista getArtista() {
        return artista;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
    }

    public Utilizador getUtilizador() {
        return utilizador;
    }

    public void setUtilizador(Utilizador utilizador) {
        this.utilizador = utilizador;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}