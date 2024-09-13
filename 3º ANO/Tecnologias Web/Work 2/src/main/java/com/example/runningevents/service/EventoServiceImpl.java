package com.example.runningevents.service;

import com.example.runningevents.model.Evento;
import com.example.runningevents.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventoServiceImpl implements EventoService{

    @Autowired
    private EventoRepository eventoRepository;

    @Override
    public Evento criarNovoEvento(Evento evento) {
        return eventoRepository.save(evento);
    }

    @Override
    public Evento atualizarEvento(Long id, Evento evento) {
        evento.setId(id);
        return eventoRepository.save(evento);
    }

    @Override
    public List<Evento> listarTodosEventos() {
        return eventoRepository.findAll();
    }

    @Override
    public Evento obterEventoPorId(Long id) {
        return eventoRepository.findById(id).orElse(null);
    }

    @Override
    public void apagarEvento(Long id) {
        eventoRepository.deleteById(id);

    }

    @Override
    public Page<Evento> procurarEventosPassados(Pageable pageable) {
        LocalDate hoje = LocalDate.now();
        return eventoRepository.findByDataBefore(hoje, pageable);
    }

    @Override
    public Page<Evento> procurarEventosAtuais(Pageable pageable) {
        LocalDate hoje = LocalDate.now();
        return eventoRepository.findByData(hoje, pageable);
    }

    @Override
    public Page<Evento> procurarEventosFuturos(Pageable pageable) {
        LocalDate hoje = LocalDate.now();
        return eventoRepository.findByDataAfter(hoje, pageable);
    }
}
