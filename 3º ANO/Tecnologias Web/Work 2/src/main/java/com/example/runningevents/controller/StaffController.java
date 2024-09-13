package com.example.runningevents.controller;

import com.example.runningevents.model.Evento;
import com.example.runningevents.model.Utilizador;
import com.example.runningevents.repository.UtilizadorRepository;
import com.example.runningevents.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/staff/")
public class StaffController {

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    @Autowired
    private EventoService eventoService;

    @ModelAttribute
    private void userDetails(Model m, Principal p) {
        String email = p.getName();
        Utilizador utlizdr = utilizadorRepository.findByEmail(email);
        m.addAttribute("utilizador", utlizdr);
    }

    @GetMapping("/")
    public String home() {
        return "staff/home";
    }

    @GetMapping("/criarEvento")
    public String pagCriarEvento() {
        return "staff/criarEvento";
    }

    @PostMapping("/criarEvento")
    public String registarEvento(
            @RequestParam("nome") String nome,
            @RequestParam("descricao") String descricao,
            @RequestParam("data") String dataString,
            @RequestParam("valor") Double valor,
            RedirectAttributes redirectAttributes
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate data = LocalDate.parse(dataString, formatter);

        Evento evento = new Evento(nome, descricao, data, valor);
        eventoService.criarNovoEvento(evento);

        redirectAttributes.addFlashAttribute("msg", "Evento registado com sucesso.");

        return "redirect:/staff/criarEvento";
    }

}
