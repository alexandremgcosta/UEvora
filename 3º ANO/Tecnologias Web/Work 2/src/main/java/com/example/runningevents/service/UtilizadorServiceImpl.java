package com.example.runningevents.service;

import com.example.runningevents.model.Utilizador;
import com.example.runningevents.repository.UtilizadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UtilizadorServiceImpl implements UtilizadorService{

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncode;

    @Override
    public Utilizador criarUtilizador(Utilizador utilizador) {
        utilizador.setPassword(passwordEncode.encode(utilizador.getPassword()));
        return utilizadorRepository.save(utilizador);
    }

    @Override
    public boolean checkEmail(String email) {
        return utilizadorRepository.existsByEmail(email);
    }
}
