package br.com.toto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.toto.model.Jogador;
import br.com.toto.repository.Jogadores;

@Controller
@RequestMapping("/jogadores")
public class JogadoresController {

	@Autowired
	private Jogadores repoJogadores;
	
	@PostMapping
	public String cadastrarJogador(Jogador jogador) {
		repoJogadores.save(jogador);
		return "redirect:/jogadores";
	}
	
	@GetMapping
	public ModelAndView listarJogadores() {
		ModelAndView mav = new ModelAndView("ListaJogadores");
		mav.addObject("jogadores",repoJogadores.findAll());
		mav.addObject(new Jogador());
		return mav;
	}
}
