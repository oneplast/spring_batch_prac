package io.devground.spring_batch_prac.domain.product.order.controller;

import static java.nio.charset.StandardCharsets.*;
import static org.springframework.http.HttpStatus.*;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import net.minidev.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.product.order.entity.Order;
import io.devground.spring_batch_prac.domain.product.order.service.OrderService;
import io.devground.spring_batch_prac.domain.product.product.entity.Product;
import io.devground.spring_batch_prac.domain.product.product.service.ProductService;
import io.devground.spring_batch_prac.global.app.AppConfig;
import io.devground.spring_batch_prac.global.exception.GlobalException;
import io.devground.spring_batch_prac.global.rq.Rq;
import io.devground.spring_batch_prac.standard.util.ParseUtils;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
@PreAuthorize("isAuthenticated()")
public class OrderController {

	private final OrderService orderService;
	private final ProductService productService;
	private final Rq rq;
	private final RestTemplate restTemplate;

	@PostMapping("/directMakeOrder/{productId}")
	public String directMakeOrder(@PathVariable long productId) {
		Product product = productService.findById(productId)
			.orElseThrow(() -> new GlobalException(BAD_REQUEST.value(), "존재하지 않는 상품입니다."));

		Order order = orderService.createFromProduct(rq.getMember(), product);

		return rq.redirect("/order/" + order.getId(), "주문이 완료되었습니다.");
	}

	@PostMapping("/createFromCart")
	public String createFromCart() {
		Order order = orderService.createFromOrder(rq.getMember());

		return rq.redirect("/order/" + order.getId(), "주문이 완료되었습니다.");
	}

	@DeleteMapping("/{id}/cancel")
	public String cancel(@PathVariable long id, String redirectUrl) {
		Order order = orderService.findById(id)
			.orElseThrow(() -> new GlobalException(NOT_FOUND.value(), "존재하지 않는 주문입니다."));

		if (!orderService.canCancel(rq.getMember(), order)) {
			throw new GlobalException(FORBIDDEN.value(), "권한이 없습니다.");
		}

		orderService.cancel(order);

		if (!StringUtils.hasText(redirectUrl)) {
			redirectUrl = "/order/" + order.getId();
		}

		return rq.redirect(redirectUrl, "주문이 취소되었습니다.");
	}

	@GetMapping("/myList")
	public String showMyList(
		@RequestParam(defaultValue = "1") int page,
		Boolean payStatus,
		Boolean cancelStatus,
		Boolean refundStatus
	) {
		List<Sort.Order> sorts = new ArrayList<>();

		sorts.add(Sort.Order.desc("id"));
		Pageable pageable = PageRequest.of(page - 1, 50, Sort.by(sorts));

		Page<Order> orderPage = orderService.search(rq.getMember(), payStatus, cancelStatus, refundStatus, pageable);

		rq.attr("orderPage", orderPage);

		return "domain/product/order/myList";
	}

	@GetMapping("/{id}")
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

	@PostMapping("/{id}/payByCash")
	public String payByCash(@PathVariable long id) {
		Order order = orderService.findById(id)
			.orElseThrow(() -> new GlobalException(NOT_FOUND.value(), "존재하지 않는 주문입니다."));

		if (!orderService.canPay(order, 0)) {
			throw new GlobalException(FORBIDDEN.value(), "권한이 없습니다.");
		}

		orderService.payByCashOnly(order);

		return rq.redirect("/order/" + order.getId(), "결제가 완료되었습니다.");
	}

	@GetMapping("/success")
	public String showSuccess() {
		return "domain/product/order/success";
	}

	@GetMapping("/fail")
	public String showFail(String failCode, String failMessage) {
		rq.attr("code", failCode);
		rq.attr("message", failMessage);

		return "domain/product/order/fail";
	}

	public record TossPaymentRequest(
		String paymentKey,
		String orderId,
		long amount
	) {
	}

	@PostMapping("/confirm")
	public ResponseEntity<JSONObject> confirmPayment(@RequestBody TossPaymentRequest request) throws Exception {

		orderService.checkCanPay(request.orderId, request.amount);

		String apiKey = AppConfig.getTossPaymentsWidgetSecretKey() + ":";
		String encryptedSecretKey = "Basic " + Base64.getEncoder().encodeToString(apiKey.getBytes(UTF_8));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set(HttpHeaders.AUTHORIZATION, encryptedSecretKey);

		Map<String, Object> requestMap = AppConfig.getObjectMapper().convertValue(request, new TypeReference<>() {
		});

		HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(requestMap, headers);
		ResponseEntity<String> response;

		try {
			response = restTemplate.exchange(
				AppConfig.getTossPaymentsTargetUrl(),
				HttpMethod.POST,
				httpEntity,
				String.class
			);

			orderService.payByTossPayments(request.orderId, request.amount);
		} catch (HttpStatusCodeException e) {
			throw new RuntimeException("결제 승인에 실패하였습니다.");
		}

		JSONObject responseBody = ParseUtils.parseToJsonObject(response.getBody());

		return ResponseEntity.status(response.getStatusCode()).body(responseBody);
	}
}
