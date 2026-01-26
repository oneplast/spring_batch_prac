package io.devground.spring_batch_prac.domain.product.order.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.product.cart.entity.CartItem;
import io.devground.spring_batch_prac.global.jpa.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "order_")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Order extends BaseEntity {

	@ManyToOne
	private Member buyer;

	@Builder.Default
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> orderItems = new ArrayList<>();

	private LocalDateTime payDate;
	private LocalDateTime cancelDate;
	private LocalDateTime refundDate;

	public void addItem(CartItem cartItem) {
		OrderItem orderItem = OrderItem.builder()
			.order(this)
			.product(cartItem.getProduct())
			.build();

		orderItems.add(orderItem);
	}

	public long calcPayPrice() {
		return orderItems.stream().mapToLong(OrderItem::getPayPrice).sum();
	}

	public void setPaymentDone() {
		payDate = LocalDateTime.now();
	}

	public void setCancelDate() {
		cancelDate = LocalDateTime.now();
	}

	public void setRefundDate() {
		refundDate = LocalDateTime.now();
	}
}
