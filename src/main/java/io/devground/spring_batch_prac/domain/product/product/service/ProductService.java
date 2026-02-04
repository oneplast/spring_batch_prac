package io.devground.spring_batch_prac.domain.product.product.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.devground.spring_batch_prac.domain.book.book.entity.Book;
import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.product.product.entity.Product;
import io.devground.spring_batch_prac.domain.product.product.repsoitory.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductBookmarkService productBookmarkService;

	@Transactional
	public Product createProduct(Book book, boolean published) {

		if (book.getProduct() != null) {
			return book.getProduct();
		}

		Product product = Product.builder()
			.maker(book.getAuthor())
			.relTypeCode(book.getModelName())
			.relId(book.getId())
			.name(book.getTitle())
			.price(book.getPrice())
			.published(published)
			.build();

		productRepository.save(product);
		book.setProduct(product);

		return product;
	}

	public Page<Product> search(Member maker, Boolean published, List<String> kwTypes, String kw, Pageable pageable) {
		return productRepository.search(maker, published, kwTypes, kw, pageable);
	}

	@Transactional
	public void bookmark(Member member, Product product) {
		productBookmarkService.createProductBookmark(member, product);
	}

	public boolean canBookmark(Member actor, Product product) {
		if (Objects.isNull(actor)) {
			return false;
		}

		return productBookmarkService.canBookmark(actor, product);
	}

	public boolean canCancelBookmark(Member actor, Product product) {
		if (Objects.isNull(actor)) {
			return false;
		}

		return productBookmarkService.canCancelBookmark(actor, product);
	}

	public Optional<Product> findById(long id) {
		return productRepository.findById(id);
	}
}
