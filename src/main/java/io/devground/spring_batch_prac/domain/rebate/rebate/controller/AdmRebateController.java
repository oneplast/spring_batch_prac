package io.devground.spring_batch_prac.domain.rebate.rebate.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.devground.spring_batch_prac.domain.product.order.entity.OrderItem;
import io.devground.spring_batch_prac.domain.product.order.service.OrderService;
import io.devground.spring_batch_prac.domain.rebate.rebate.service.RebateService;
import io.devground.spring_batch_prac.global.rq.Rq;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adm/rebate")
public class AdmRebateController {

	private final RebateService rebateService;
	private final OrderService orderService;
	private final Rq rq;

	@GetMapping("/make")
	public String showMake() {

		return "domain/rebate/rebate/adm/make";
	}

	@PostMapping("/make")
	public String make() {

		List<OrderItem> orderItems = orderService.findItems();

		rebateService.make(orderItems);

		return rq.redirect("/adm/rebate/make", "정산 데이터를 생성했습니다.");
	}
}
