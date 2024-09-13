package com.example.SeekArtist.model.localizacao;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LocalizacaoRequestDTO(String username_artista, double longitude, double latitude, LocalDateTime data) {
}