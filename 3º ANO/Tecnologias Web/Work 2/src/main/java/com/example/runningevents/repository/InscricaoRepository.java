package com.example.runningevents.repository;

import com.example.runningevents.model.Inscricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {
    boolean existsByEventoIdAndParticipanteId(Long eventoId, Long participanteId);
}
