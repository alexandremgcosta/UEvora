package com.example.SeekArtist.controller;

import com.example.SeekArtist.config.TokenService;
import com.example.SeekArtist.model.utilizador.AuthenticationDTO;
import com.example.SeekArtist.model.utilizador.LoginResponseDTO;
import com.example.SeekArtist.model.utilizador.RegisterDTO;
import com.example.SeekArtist.model.utilizador.Utilizador;
import com.example.SeekArtist.repository.UtilizadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Utilizador) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
        if ((this.utilizadorRepository.findByUsername(data.username()) != null) || (this.utilizadorRepository.findByEmail(data.email()) != null)) {
            return ResponseEntity.badRequest().build();
        } else {
            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
            Utilizador utilizador = new Utilizador(data.username(), data.email(), encryptedPassword, data.role());
            this.utilizadorRepository.save(utilizador);
            return ResponseEntity.ok().build();
        }
    }
}
