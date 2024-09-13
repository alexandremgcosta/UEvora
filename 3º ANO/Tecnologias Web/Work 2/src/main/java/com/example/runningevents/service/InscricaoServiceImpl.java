package com.example.runningevents.service;

import com.example.runningevents.model.Inscricao;
import com.example.runningevents.repository.InscricaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InscricaoServiceImpl implements InscricaoService {
    @Autowired
    private InscricaoRepository inscricaoRepository;

    @Override
    public Inscricao criarInscricao(Inscricao inscricao) {
        return inscricaoRepository.save(inscricao);
    }

    @Override
    public boolean existeInscricao(Long eventoId, Long participanteId) {
        return inscricaoRepository.existsByEventoIdAndParticipanteId(eventoId, participanteId);
    }
}
