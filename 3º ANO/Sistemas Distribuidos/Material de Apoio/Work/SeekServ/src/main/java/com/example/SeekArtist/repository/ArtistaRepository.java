package com.example.SeekArtist.repository;

import com.example.SeekArtist.model.artista.Artista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtistaRepository extends JpaRepository<Artista, Long> {

    Artista findByUsername(String username);

    List<Artista> findByArte(String arte);

    List<Artista> findByEstado(String estado);

}
