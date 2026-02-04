package io.devground.spring_batch_prac.domain.product.product.service;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.product.product.entity.Product;
import io.devground.spring_batch_prac.domain.product.product.entity.ProductBookmark;
import io.devground.spring_batch_prac.domain.product.product.repsoitory.ProductBookmarkRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductBookmarkService {

	private final ProductBookmarkRepository productBookmarkRepository;

	@Transactional
	public void bookmark(Member member, Product product) {

		ProductBookmark productBookmark = ProductBookmark.builder()
			.member(member)
			.product(product)
			.build();

		productBookmarkRepository.save(productBookmark);
	}

	@Transactional
	public void cancelBookmark(Member actor, Product product) {
		productBookmarkRepository.deleteByMemberAndProduct(actor, product);
	}

	public boolean canBookmark(Member actor, Product product) {
		if (Objects.isNull(actor)) {
			return false;
		}

		return !productBookmarkRepository.existsByMemberAndProduct(actor, product);
	}

	public boolean canCancelBookmark(Member actor, Product product) {
		if (Objects.isNull(actor)) {
			return false;
		}

		return productBookmarkRepository.existsByMemberAndProduct(actor, product);
	}
}
