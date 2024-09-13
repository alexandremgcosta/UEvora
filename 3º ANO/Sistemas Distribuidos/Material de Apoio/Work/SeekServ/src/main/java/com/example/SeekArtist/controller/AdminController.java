package com.example.SeekArtist.controller;

import com.example.SeekArtist.model.artista.Artista;
import com.example.SeekArtist.model.artista.ArtistaRequestDTO;
import com.example.SeekArtist.model.artista.ArtistaUpdateDTO;
import com.example.SeekArtist.model.utilizador.UserRole;
import com.example.SeekArtist.model.utilizador.Utilizador;
import com.example.SeekArtist.repository.ArtistaRepository;
import com.example.SeekArtist.repository.UtilizadorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    @PutMapping("/darPermissaoAdmin/{user}")
    public ResponseEntity<?> darPermissaoAdmin(@PathVariable String user) {
        UserDetails userDetails = utilizadorRepository.findByUsername(user);
        Utilizador utilizador = (Utilizador) userDetails;
        if (utilizador == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilizador não encontrado");
        }

        utilizador.setRole(UserRole.ADMIN);
        utilizadorRepository.save(utilizador);

        return ResponseEntity.ok("Permissão de administrador concedida ao utilizador com sucesso");
    }

    @PostMapping("/listarPorEstado")
    public ResponseEntity<?> listarArtistasPorEstado(@RequestBody @Valid ArtistaRequestDTO body) {
        String estado = body.estado();
        if (!estado.equals("aprovado") && !estado.equals("nao aprovado")) {
            return ResponseEntity.badRequest().body("Estado inválido. Os estados permitidos são 'aprovado' ou 'nao aprovado'.");
        }

        List<Artista> artistas = artistaRepository.findByEstado(estado);
        return ResponseEntity.ok(artistas);
    }

    @PostMapping("/aprovarArtista")
    public ResponseEntity<?> aprovarArtista(@RequestBody @Valid ArtistaRequestDTO body) {
        Artista artista = artistaRepository.findByUsername(body.username());

        if (artista == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artista não encontrado");
        }
        if (!artista.getEstado().equals("nao aprovado")) {
            return ResponseEntity.badRequest().body("Artista já está aprovado ou o estado é inválido");
        }
        artista.setEstado("aprovado");
        artistaRepository.save(artista);

        return ResponseEntity.ok("Artista aprovado com sucesso");
    }

    @PostMapping("/consultarDadosArtista")
    public ResponseEntity<?> consultarDadosArtista(@RequestBody @Valid ArtistaRequestDTO body) {
        Artista artista = artistaRepository.findByUsername(body.username());
        if (artista == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Username de Artista não encontrado");
        }

        return ResponseEntity.ok(artista);
    }

    @PutMapping("/atualizar/{username}")
    public ResponseEntity<?> atualizarArtista(@PathVariable String username, @RequestBody @Valid ArtistaUpdateDTO artistaUpdateDTO) {
        Artista artista = artistaRepository.findByUsername(username);
        if (artista == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artista não encontrado");
        }

        if (artistaUpdateDTO.estado() != null && (artistaUpdateDTO.estado().equals("aprovado") || artistaUpdateDTO.estado().equals("nao aprovado"))) {
            artista.setEstado(artistaUpdateDTO.estado());
        }

        if (artistaUpdateDTO.arte() != null) {
            artista.setArte(artistaUpdateDTO.arte());
        }

        artistaRepository.save(artista);

        return ResponseEntity.ok("Informações do artista atualizadas com sucesso");
    }

}



