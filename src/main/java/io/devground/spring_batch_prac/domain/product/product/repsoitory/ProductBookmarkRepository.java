package io.devground.spring_batch_prac.domain.product.product.repsoitory;

import org.springframework.data.jpa.repository.JpaRepository;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.product.product.entity.Product;
import io.devground.spring_batch_prac.domain.product.product.entity.ProductBookmark;

public interface ProductBookmarkRepository extends JpaRepository<ProductBookmark, Long> {

	boolean existsByMemberAndProduct(Member actor, Product product);
}
