package com.example.SeekArtist.model.artista;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ArtistaRequestDTO(Long Id, String username, String estado, String arte, Double longitude, Double latitude, LocalDateTime data) {

}
