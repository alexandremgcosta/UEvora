package com.example.runningevents.service;

import com.example.runningevents.model.Utilizador;

public interface UtilizadorService {

    public Utilizador criarUtilizador(Utilizador utilizador);

    public boolean checkEmail(String email);
}
