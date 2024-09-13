package com.example.runningevents.service;

import com.example.runningevents.model.Inscricao;

public interface InscricaoService {
    Inscricao criarInscricao(Inscricao inscricao);
    public boolean existeInscricao(Long eventoId, Long participanteId);
}
