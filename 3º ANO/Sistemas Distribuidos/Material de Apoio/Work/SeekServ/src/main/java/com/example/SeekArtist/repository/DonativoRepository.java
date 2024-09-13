package com.example.SeekArtist.repository;

import com.example.SeekArtist.model.donativo.Donativo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DonativoRepository extends JpaRepository<Donativo, Long> {

    List<Donativo> findByArtistaId(Long artistaId);
}
