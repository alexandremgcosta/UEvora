package com.example.SeekArtist.repository;

import com.example.SeekArtist.model.utilizador.Utilizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UtilizadorRepository extends JpaRepository<Utilizador, Long> {

    UserDetails findByUsername(String username);

    UserDetails findByEmail(String email);
}
