package io.devground.spring_batch_prac.domain.product.order.controller;

import static org.springframework.http.HttpStatus.*;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.product.order.entity.Order;
import io.devground.spring_batch_prac.domain.product.order.service.OrderService;
import io.devground.spring_batch_prac.global.exception.GlobalException;
import io.devground.spring_batch_prac.global.rq.Rq;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

	private final OrderService orderService;
	private final Rq rq;

	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public String showDetail(@PathVariable long id, Model model) {
		Order order = orderService.findById(id)
			.orElseThrow(() -> new GlobalException(NOT_FOUND.value(), "존재하지 않는 주문입니다."));

		Member actor = rq.getMember();

		long restCash = actor.getRestCash();

		if (!orderService.actorCanSee(actor, order)) {
			throw new GlobalException(FORBIDDEN.value(), "권한이 없습니다.");
		}

		model.addAttribute("order", order);
		model.addAttribute("actorRestCash", restCash);

		return "domain/product/order/detail";
	}
}
