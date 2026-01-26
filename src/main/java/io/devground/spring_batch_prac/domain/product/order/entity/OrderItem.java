package io.devground.spring_batch_prac.domain.product.order.entity;

import io.devground.spring_batch_prac.domain.product.product.entity.Product;
import io.devground.spring_batch_prac.global.jpa.BaseEntity;
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
public class OrderItem extends BaseEntity {

	@ManyToOne
	private Order order;
	@ManyToOne
	private Product product;

	public long getPayPrice() {
		return product.getPrice();
	}
}
