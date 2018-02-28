package br.com.toto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.toto.model.Time;
import br.com.toto.repository.Times;

@Controller
@RequestMapping("/times")
public class TimesController {

	@Autowired
	private Times repoTimes;
	
	@PostMapping
	public String cadastrarJogador(Time time) {
		repoTimes.save(time);
		return "redirect:/times";
	}
	
	@GetMapping
	public ModelAndView listarTimes() {
		ModelAndView mav = new ModelAndView("ListaTimes");
		mav.addObject("times",repoTimes.findAll());
		mav.addObject(new Time());
		return mav;
	}
}
