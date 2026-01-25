package io.devground.spring_batch_prac.domain.product.product.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.devground.spring_batch_prac.domain.book.book.entity.Book;
import io.devground.spring_batch_prac.domain.product.product.entity.Product;
import io.devground.spring_batch_prac.domain.product.product.repsoitory.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

	private final ProductRepository productRepository;

	@Transactional
	public Product createBook(Book book) {

		if (book.getProduct() != null) {
			return book.getProduct();
		}

		Product product = Product.builder()
			.maker(book.getAuthor())
			.relTypeCode(book.getModelName())
			.relId(book.getId())
			.name(book.getTitle())
			.price(book.getPrice())
			.build();

		productRepository.save(product);
		book.setProduct(product);

		return product;
	}
}
