package io.devground.spring_batch_prac.domain.product.order.entity;

import io.devground.spring_batch_prac.domain.product.product.entity.Product;
import io.devground.spring_batch_prac.global.jpa.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class OrderItem extends BaseTime {

	@ManyToOne
	private Order order;
	@ManyToOne
	private Product product;
	private int payPrice;

	private double rebateRate;

	public long getPayPrice() {
		return product.getPrice();
	}

	public void setPaymentDone() {
		switch (product.getRelTypeCode()) {
			case "book" -> order.getBuyer().addMyBook(product.getBook());
		}
	}

	public void setCancelDone() {
	}

	public void setRefundDone() {
		switch (product.getRelTypeCode()) {
			case "book" -> order.getBuyer().removeMyBook(product.getBook());
		}
	}
}
