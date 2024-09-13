package com.example.runningevents.repository;

import com.example.runningevents.model.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtilizadorRepository extends JpaRepository<Utilizador, Long> {
    boolean existsByEmail(String email);
    public Utilizador findByEmail(String email);
}
