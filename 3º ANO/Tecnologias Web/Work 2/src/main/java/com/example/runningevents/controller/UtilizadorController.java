package com.example.runningevents.controller;

import com.example.runningevents.model.Evento;
import com.example.runningevents.model.Inscricao;
import com.example.runningevents.model.Utilizador;
import com.example.runningevents.repository.UtilizadorRepository;
import com.example.runningevents.service.EventoService;
import com.example.runningevents.service.InscricaoService;
import com.example.runningevents.service.UtilizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/utilizador/")
public class UtilizadorController {

    @Autowired
    private UtilizadorRepository utilizadorRepository;

    @Autowired
    private EventoService eventoService;

    @Autowired
    private InscricaoService inscricaoService;

    @Autowired
    private UtilizadorService utilizadorService;

    @ModelAttribute
    private void userDetails(Model m, Principal p){
        String email= p.getName();
        Utilizador utlizdr = utilizadorRepository.findByEmail(email);
        m.addAttribute("utilizador", utlizdr);
    }

    @GetMapping("/")
    public String home(){
        return "utilizador/home";
    }

    @GetMapping("/criarInscricao")
    public String pagCriarInscricao(Model model) {
        List<Evento> eventos = eventoService.listarTodosEventos();
        model.addAttribute("eventos", eventos);
        model.addAttribute("inscricao", new Inscricao());

        return "utilizador/criarInscricao";
    }

    @PostMapping("/criarInscricao")
    public String processarInscricao(@ModelAttribute Inscricao inscricao, @RequestParam("eventoId") Long eventoId , Principal p, RedirectAttributes redirectAttributes) {
        String email = p.getName();
        Utilizador utilizador = utilizadorRepository.findByEmail(email);

        Evento evento = eventoService.obterEventoPorId(eventoId);

        if (utilizador != null && evento != null) {
            if (!inscricaoService.existeInscricao(eventoId, utilizador.getId())) {
                inscricao.setParticipante(utilizador);
                inscricao.setEvento(evento);
                inscricaoService.criarInscricao(inscricao);
                redirectAttributes.addFlashAttribute("msg", "Inscrição realizada com sucesso!");
            } else {
                redirectAttributes.addFlashAttribute("msg", "Você já está inscrito neste evento.");
            }
        } else {
            redirectAttributes.addFlashAttribute("msg", "Erro ao realizar inscrição.");
        }

        return "redirect:/utilizador/criarInscricao";
    }


}
