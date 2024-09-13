package com.example.SeekArtist.model.localizacao;

import com.example.SeekArtist.model.artista.Artista;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Localizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private double longitude;

    @Column
    private double latitude;

    @Column
    private LocalDateTime data;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "artista_id")
    private Artista artista;

    public Localizacao() {
    }

    public Localizacao(LocalizacaoRequestDTO data) {
        this.longitude = data.longitude();
        this.latitude = data.latitude();
        this.data = data.data();
    }

   public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
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
}