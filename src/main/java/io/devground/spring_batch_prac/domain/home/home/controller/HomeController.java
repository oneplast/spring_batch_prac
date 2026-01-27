package io.devground.spring_batch_prac.domain.home.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.devground.spring_batch_prac.global.rq.Rq;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

	private final Rq rq;

	@GetMapping("/")
	public String showMain() {
		return "domain/home/home/main";
	}
}
