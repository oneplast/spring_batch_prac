package io.devground.spring_batch_prac.domain.product.product.repsoitory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.product.product.entity.Product;

public interface ProductRepositoryCustom {

	Page<Product> search(Member maker, Boolean published, List<String> kwTypes, String kw, Pageable pageable);
}
