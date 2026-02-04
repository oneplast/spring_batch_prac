package io.devground.spring_batch_prac.domain.product.product.controller;

import static org.springframework.http.HttpStatus.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.devground.spring_batch_prac.domain.product.product.entity.Product;
import io.devground.spring_batch_prac.domain.product.product.service.ProductService;
import io.devground.spring_batch_prac.global.exception.GlobalException;
import io.devground.spring_batch_prac.global.rq.Rq;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

	private final Rq rq;
	private final ProductService productService;

	@GetMapping("/list")
	public String list(
		@RequestParam(value = "kwType", defaultValue = "name") List<String> kwTypes,
		@RequestParam(defaultValue = "") String kw,
		@RequestParam(defaultValue = "1") int page,
		Model model
	) {

		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("id"));
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(sorts));

		Map<String, Boolean> kwTypesMap = kwTypes.stream()
			.collect(Collectors.toMap(
				kwType -> kwType,
				kwType -> true
			));

		Page<Product> itemsPage = productService.search(null, true, kwTypes, kw, pageable);
		model.addAttribute("itemPage", itemsPage);
		model.addAttribute("kwTypesMap", kwTypesMap);
		model.addAttribute("page", page);

		return "domain/product/product/list";
	}

	@GetMapping("/{id}")
	@PreAuthorize("isAuthenticated()")
	public String showDetail(@PathVariable long id) {
		return null;
	}

	@PostMapping("/{id}/bookmark")
	@PreAuthorize("isAuthenticated()")
	public String bookmark(@PathVariable long id, @RequestParam(defaultValue = "/") String redirectUrl) {

		Product product = productService.findById(id)
			.orElseThrow(() -> new GlobalException(BAD_REQUEST.value(), "존재하지 않는 상품입니다."));

		productService.bookmark(rq.getMember(), product);

		return rq.redirect(redirectUrl, null);
	}
}
