package br.com.toto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.toto.model.Campeonato;
import br.com.toto.repository.Campeonatos;

@Controller
@RequestMapping("/campeonatos")
public class CampeonatosController {

	@Autowired
	private Campeonatos repoCampeonatos;
	
	@PostMapping
	public String cadastrarJogador(Campeonato campeonato) {
		repoCampeonatos.save(campeonato);
		return "redirect:/campeonatos";
	}
	
	@GetMapping
	public ModelAndView listarJogadores() {
		ModelAndView mav = new ModelAndView("ListaCampeonatos");
		mav.addObject("campeonatos",repoCampeonatos.findAll());
		mav.addObject(new Campeonato());
		return mav;
	}
}
