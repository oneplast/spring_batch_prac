package io.devground.spring_batch_prac.domain.product.product.repsoitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.product.product.entity.Product;
import io.devground.spring_batch_prac.domain.product.product.entity.ProductBookmark;

public interface ProductBookmarkRepository extends JpaRepository<ProductBookmark, Long> {

	List<ProductBookmark> findByMemberOrderByIdDesc(Member actor);

	boolean existsByMemberAndProduct(Member actor, Product product);

	void deleteByMemberAndProduct(Member actor, Product product);
}
