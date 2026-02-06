package io.devground.spring_batch_prac.domain.rebate.rebate.entity;

import java.time.LocalDateTime;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.product.order.entity.OrderItem;
import io.devground.spring_batch_prac.domain.product.product.entity.Product;
import io.devground.spring_batch_prac.global.jpa.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class RebateItem extends BaseTime {

	@OneToOne
	private OrderItem orderItem;

	@ManyToOne
	private Member seller;
	@ManyToOne
	private Member buyer;

	@ManyToOne
	private Product product;
	private long payPrice;
	private LocalDateTime eventDate;

	private double rebateRate;
	private long rebatePrice;
	private LocalDateTime rebateDate;
}
