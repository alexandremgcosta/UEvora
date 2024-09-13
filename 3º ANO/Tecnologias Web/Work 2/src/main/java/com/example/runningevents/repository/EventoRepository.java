package com.example.runningevents.repository;

import com.example.runningevents.model.Evento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    Page<Evento> findByDataBefore(LocalDate data, Pageable pageable);
    Page<Evento> findByData(LocalDate data, Pageable pageable);
    Page<Evento> findByDataAfter(LocalDate data, Pageable pageable);
}
