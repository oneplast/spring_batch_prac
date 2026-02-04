package io.devground.spring_batch_prac.domain.product.product.repsoitory;

import static io.devground.spring_batch_prac.domain.product.product.entity.QProduct.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.product.product.entity.Product;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<Product> search(Member maker, Boolean published, List<String> kwTypes, String kw, Pageable pageable) {
		BooleanBuilder builder = new BooleanBuilder();

		if (maker != null) {
			builder.and(product.maker.eq(maker));
		}

		if (published != null) {
			builder.and(product.published.eq(published));
		}

		if (StringUtils.hasText(kw)) {
			List<BooleanExpression> conditions = new ArrayList<>();

			if (kwTypes.contains("name")) {
				conditions.add(product.name.containsIgnoreCase(kw));
			}

			conditions.stream().reduce(BooleanExpression::or).ifPresent(builder::and);

		}

		JPAQuery<Product> productQuery = jpaQueryFactory
			.selectFrom(product)
			.where(builder);

		for (Sort.Order o : pageable.getSort()) {
			PathBuilder pathBuilder = new PathBuilder(product.getType(), product.getMetadata());
			productQuery.orderBy(
				new OrderSpecifier(
					o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty())
				)
			);
		}

		productQuery.offset(pageable.getOffset()).limit(pageable.getPageSize());

		JPAQuery<Long> totalQuery = jpaQueryFactory
			.select(product.count())
			.from(product)
			.where(builder);

		return PageableExecutionUtils.getPage(productQuery.fetch(), pageable, totalQuery::fetchOne);
	}
}
