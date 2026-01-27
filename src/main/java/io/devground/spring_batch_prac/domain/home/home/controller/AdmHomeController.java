package io.devground.spring_batch_prac.domain.home.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adm")
public class AdmHomeController {

	@GetMapping
	public String showMain() {
		return "domain/home/home/adm/main";
	}
}
