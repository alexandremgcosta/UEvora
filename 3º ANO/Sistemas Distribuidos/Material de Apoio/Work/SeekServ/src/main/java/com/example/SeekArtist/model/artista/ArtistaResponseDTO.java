package com.example.SeekArtist.model.artista;

import com.example.SeekArtist.model.artista.Artista;

public record ArtistaResponseDTO(Long id, String username, String estado, String arte) {
    public ArtistaResponseDTO(Artista artista){
        this(artista.getId(), artista.getUsername(), artista.getEstado(), artista.getArte());
    }
}
