package com.example.SeekArtist.model.localizacao;

import com.example.SeekArtist.model.localizacao.Localizacao;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LocalizacaoResponseDTO(Long id, double longitude, double latitude, LocalDateTime data, Long artistaId) {
    public LocalizacaoResponseDTO(Localizacao localizacao) {
        this(localizacao.getId(), localizacao.getLongitude(), localizacao.getLatitude(), localizacao.getData(),
                localizacao.getArtista() != null ? localizacao.getArtista().getId() : null);
    }
}