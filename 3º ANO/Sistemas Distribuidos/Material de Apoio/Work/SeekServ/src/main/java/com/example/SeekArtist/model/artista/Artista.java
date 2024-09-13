package com.example.SeekArtist.model.artista;

import com.example.SeekArtist.model.donativo.Donativo;
import com.example.SeekArtist.model.localizacao.Localizacao;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Artista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String estado;

    private String arte;

    public Artista() {
    }

    @OneToMany(mappedBy = "artista")
    @JsonManagedReference
    private List<Localizacao> localizacoes;

    @OneToMany(mappedBy = "artista")
    @JsonManagedReference
    private List<Donativo> donativo;

    public Artista(ArtistaRequestDTO data) {
        this.username = data.username();
        this.estado = data.estado();
        this.arte = data.arte();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getArte() {
        return arte;
    }

    public void setArte(String arte) {
        this.arte = arte;
    }

    public List<Localizacao> getLocalizacoes() {
        return localizacoes;
    }

    public void setLocalizacoes(List<Localizacao> localizacoes) {
        this.localizacoes = localizacoes;
    }

    public List<Donativo> getDonativo() {
        return donativo;
    }

    public void setDonativo(List<Donativo> donativo) {
        this.donativo = donativo;
    }
}