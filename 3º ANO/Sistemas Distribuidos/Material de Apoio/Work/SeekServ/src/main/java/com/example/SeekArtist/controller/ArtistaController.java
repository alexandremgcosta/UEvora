package com.example.SeekArtist.controller;

import com.example.SeekArtist.model.artista.Artista;
import com.example.SeekArtist.model.artista.ArtistaFiltroDTO;
import com.example.SeekArtist.model.artista.ArtistaRequestDTO;
import com.example.SeekArtist.model.donativo.Donativo;
import com.example.SeekArtist.model.donativo.DonativoRequestDTO;
import com.example.SeekArtist.model.localizacao.Localizacao;
import com.example.SeekArtist.model.localizacao.LocalizacaoRequestDTO;
import com.example.SeekArtist.model.utilizador.Utilizador;
import com.example.SeekArtist.repository.ArtistaRepository;
import com.example.SeekArtist.repository.DonativoRepository;
import com.example.SeekArtist.repository.LocalizacaoRepository;
import com.example.SeekArtist.repository.UtilizadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("artistas")
public class ArtistaController {

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private LocalizacaoRepository localizacaoRepository;

    @Autowired
    private DonativoRepository donativoRepository;


    //Registo artista ou adiciono localização a já existente
    @PostMapping("/register")
    public ResponseEntity registarArtista(@RequestBody @Valid ArtistaRequestDTO body) {

        LocalDateTime dataFornecida = body.data();
        if (dataFornecida.isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data fornecida deve ser futura.");
        }

        Artista artista = artistaRepository.findByUsername(body.username());

        if (artista == null) {
            artista = new Artista(body);
            artista.setEstado("nao aprovado");
            artista = this.artistaRepository.save(artista);
        }

        Localizacao localizacao = new Localizacao();
        localizacao.setLongitude(body.longitude());
        localizacao.setLatitude(body.latitude());
        localizacao.setData(body.data());
        localizacao.setArtista(artista);
        this.localizacaoRepository.save(localizacao);

        return ResponseEntity.ok().build();
    }

    // Adiciono localização a artista que já exista
    @PostMapping("/adicionarLocalizacaoFutura")
    public ResponseEntity adicionarLocalizacao(@RequestBody @Valid LocalizacaoRequestDTO body) {

        Artista artista = artistaRepository.findByUsername(body.username_artista());
        if (artista == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artista não encontrado");
        }

        // Verifica se a data fornecida é posterior à data atual
        LocalDateTime dataFornecida = body.data();
        if (dataFornecida.isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data fornecida deve ser futura.");
        }

        Localizacao localizacao = new Localizacao();
        localizacao.setLongitude(body.longitude());
        localizacao.setLatitude(body.latitude());
        localizacao.setData(body.data());
        localizacao.setArtista(artista);
        artista.getLocalizacoes().add(localizacao);
        localizacaoRepository.save(localizacao);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/filtrarPorArteELocalizacao")
    public ResponseEntity<List<Artista>> filtrarArtistas(@RequestBody @Valid ArtistaFiltroDTO body) {

        List<Artista> artistasFiltrados;

        // Primeiro, filtra por arte
        if (body.arte() != null && !body.arte().isEmpty()) {
            artistasFiltrados = artistaRepository.findByArte(body.arte());
        } else {
            artistasFiltrados = artistaRepository.findAll();
        }

        // Agora, filtra por localização, se os parâmetros forem fornecidos
        if (body.longitude() != null && body.latitude() != null) {
            List<Artista> artistasFiltradosPorLocalizacao = new ArrayList<>();
            for (Artista artista : artistasFiltrados) {
                for (Localizacao localizacao : artista.getLocalizacoes()) {
                    if ((localizacao.getLongitude() == body.longitude() ) && (localizacao.getLatitude() == body.latitude())) {
                        artistasFiltradosPorLocalizacao.add(artista);
                        break;
                    }
                }
            }
            artistasFiltrados = artistasFiltradosPorLocalizacao;
        }

        return ResponseEntity.ok(artistasFiltrados);
    }


    @GetMapping("/listaAtuacoesNoMomento")
    public ResponseEntity<List<Localizacao>> listasLocalizacoesAtuais() {

        List<Artista> todosArtistas = artistaRepository.findAll();
        LocalDateTime agora = LocalDateTime.now();

        List<Localizacao> localizacoesAtuandoHoje = new ArrayList<>();
        for (Artista artista : todosArtistas) {

            Localizacao localizacaoMaisProxima = null;

            for (Localizacao localizacao : artista.getLocalizacoes()) {
                // Verifica se é a localização mais próxima e se é para hoje
                if (localizacao.getData().toLocalDate().isEqual(agora.toLocalDate()) &&
                        (localizacaoMaisProxima == null || localizacao.getData().isAfter(localizacaoMaisProxima.getData())) &&
                        localizacao.getData().isBefore(agora)) {

                    localizacaoMaisProxima = localizacao;
                }
            }

            if (localizacaoMaisProxima != null) {
                localizacoesAtuandoHoje.add(localizacaoMaisProxima);
            }
        }

        return ResponseEntity.ok(localizacoesAtuandoHoje);
    }

    @PostMapping("/listaLocalizacoesPassadas")
    public ResponseEntity<?> listaLocalizacoesPassadas(@RequestBody @Valid ArtistaRequestDTO body) {
        Artista artista = artistaRepository.findById(body.Id()).orElse(null);
        if (artista == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artista não encontrado");
        }

        List<Localizacao> atuacoes = artista.getLocalizacoes();
        return ResponseEntity.ok(atuacoes);
    }

    @PostMapping("/listaLocalizacaoFutura")
    public ResponseEntity<?> listaLocalizacaoFutura(@RequestBody @Valid ArtistaRequestDTO body) {
        Artista artista = artistaRepository.findById(body.Id()).orElse(null);
        if (artista == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artista não encontrado");
        }

        LocalDateTime agora = LocalDateTime.now();
        Localizacao proximaLocalizacao = null;

        for (Localizacao localizacao : artista.getLocalizacoes()) {
            if (localizacao.getData().isAfter(agora) &&
                    (proximaLocalizacao == null || localizacao.getData().isBefore(proximaLocalizacao.getData()))) {
                proximaLocalizacao = localizacao;
            }
        }

        if (proximaLocalizacao == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma atuação futura encontrada");
        }

        return ResponseEntity.ok(proximaLocalizacao);
    }

    @PostMapping("/fazerDonativo")
    public ResponseEntity<?> fazerDonativo(@RequestBody @Valid DonativoRequestDTO body, Authentication authentication) {
        Artista artista = artistaRepository.findById(body.artista_id()).orElse(null);
        if (artista == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artista não encontrado");
        }

        // Obter o utilizador autenticado
        String username = authentication.getName();
        UserDetails userDetails = utilizadorRepository.findByUsername(username);
        if (userDetails instanceof Utilizador) {
            Utilizador utilizador = (Utilizador) userDetails;

            Donativo donativo = new Donativo(artista, utilizador, body.valor(), body.data());
            donativoRepository.save(donativo);

            return ResponseEntity.ok("Donativo enviado com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilizador não encontrado");
        }
    }

    @PostMapping("/listarDonativos")
    public ResponseEntity<?> listarDonativos(@RequestBody @Valid DonativoRequestDTO body) {
        if (!artistaRepository.existsById(body.artista_id())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Artista não encontrado");
        }

        List<Donativo> donativos = donativoRepository.findByArtistaId(body.artista_id());
        return ResponseEntity.ok(donativos);
    }

}
