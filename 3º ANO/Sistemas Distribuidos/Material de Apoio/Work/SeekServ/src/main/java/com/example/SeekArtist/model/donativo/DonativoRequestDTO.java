package com.example.SeekArtist.model.donativo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DonativoRequestDTO(Long artista_id, Long utilizador_id, BigDecimal valor, LocalDateTime data) {
}
