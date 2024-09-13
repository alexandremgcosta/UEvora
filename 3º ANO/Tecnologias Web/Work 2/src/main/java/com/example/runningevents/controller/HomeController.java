package com.example.runningevents.controller;

import com.example.runningevents.model.Evento;
import com.example.runningevents.model.Utilizador;
import com.example.runningevents.service.EventoService;
import com.example.runningevents.service.UtilizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UtilizadorService utilizadorService;

    @Autowired
    private EventoService eventoService;

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int pageAtual,
                        @RequestParam(defaultValue = "0") int pageFuturo,
                        @RequestParam(defaultValue = "0") int pagePassado) {
        Pageable pageableAtual = PageRequest.of(pageAtual, 4);
        Pageable pageableFuturo = PageRequest.of(pageFuturo, 4);
        Pageable pageablePassado = PageRequest.of(pagePassado, 4);

        Page<Evento> eventosAtuais = eventoService.procurarEventosAtuais(pageableAtual);
        Page<Evento> eventosFuturos = eventoService.procurarEventosFuturos(pageableFuturo);
        Page<Evento> eventosPassados = eventoService.procurarEventosPassados(pageablePassado);

        model.addAttribute("eventosAtuais", eventosAtuais);
        model.addAttribute("eventosFuturos", eventosFuturos);
        model.addAttribute("eventosPassados", eventosPassados);

        return "index";
    }

    @GetMapping("/signin")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/criarUtilizador")
    public String criarUtilizador(@ModelAttribute Utilizador utilizador, RedirectAttributes redirectAttributes) {

        boolean emailexiste = utilizadorService.checkEmail(utilizador.getEmail());

        if (emailexiste) {
            redirectAttributes.addFlashAttribute("msg", "Email já existente.");
        } else {
            Utilizador utlzdr = utilizadorService.criarUtilizador(utilizador);
            if (utlzdr != null) {
                redirectAttributes.addFlashAttribute("msg", "Registo efetuado com sucesso.");
            } else {
                redirectAttributes.addFlashAttribute("msg", "Registo não efetuado.");
            }
        }

        return "redirect:/register";
    }

}
