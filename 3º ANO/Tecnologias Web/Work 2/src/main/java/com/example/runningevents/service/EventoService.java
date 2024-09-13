package com.example.runningevents.service;

import com.example.runningevents.model.Evento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventoService {

    Evento criarNovoEvento(Evento evento);
    Evento atualizarEvento(Long id, Evento evento);
    List<Evento> listarTodosEventos();
    Evento obterEventoPorId(Long id);
    void apagarEvento(Long id);

    public Page<Evento> procurarEventosPassados(Pageable pageable);

    public Page<Evento> procurarEventosAtuais(Pageable pageable);

    public Page<Evento> procurarEventosFuturos(Pageable pageable);
}
