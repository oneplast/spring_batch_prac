package io.devground.spring_batch_prac.domain.product.order.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.domain.product.cart.entity.CartItem;
import io.devground.spring_batch_prac.global.app.AppConfig;
import io.devground.spring_batch_prac.global.exception.GlobalException;
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
	@ToString.Include
	private List<OrderItem> orderItems = new ArrayList<>();

	private LocalDateTime payDate;
	private LocalDateTime cancelDate;
	private LocalDateTime refundDate;

	public void addItem(CartItem cartItem) {
		if (buyer.has(cartItem.getProduct())) {
			throw new GlobalException(HttpStatus.BAD_REQUEST.value(), "이미 구매한 상풉입니다.");
		}

		OrderItem orderItem = OrderItem.builder()
			.order(this)
			.product(cartItem.getProduct())
			.build();

		orderItems.add(orderItem);
	}

	public long calcPayPrice() {
		return orderItems.stream().mapToLong(OrderItem::getPayPrice).sum();
	}

	public String getName() {
		String name = orderItems.getFirst().getProduct().getName();

		if (orderItems.size() > 1) {
			name += "외 %d건".formatted(orderItems.size() - 1);
		}

		return name;
	}

	public String getCode() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		return getCreateDate().format(formatter) + (
			AppConfig.isNotProd()
				? "-test-" + UUID.randomUUID()
				: ""
		) + "__" + getId();
	}

	public String getForPrintPayStatus() {
		if (payDate == null) {
			return "결제대기";
		}

		return "결제완료(" + payDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")";
	}

	public String getForPrintCancelStatus() {
		if (!isCancelable()) {
			return "취소불가능";
		}

		if (cancelDate == null) {
			return "취소가능";
		}

		return "취소완료(" + cancelDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")";
	}

	public String getForPrintRefundStatus() {
		if (payDate == null) {
			return "-";
		}

		if (!isCancelable()) {
			return "-";
		}

		if (refundDate == null) {
			return "환불가능";
		}

		return "환불완료(" + refundDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")";
	}

	public void setPaymentDone() {
		payDate = LocalDateTime.now();

		orderItems.forEach(OrderItem::setPaymentDone);
	}

	public void setCancelDate() {
		cancelDate = LocalDateTime.now();

		orderItems.forEach(OrderItem::setCancelDone);
	}

	public void setRefundDate() {
		refundDate = LocalDateTime.now();

		orderItems.forEach(OrderItem::setRefundDone);
	}

	public boolean isPayable() {
		if (payDate != null) {
			return false;
		}

		return cancelDate == null;
	}

	public boolean isPayDone() {
		return payDate != null;
	}

	public boolean isCancelable() {
		if (cancelDate != null) {
			return false;
		}

		return payDate == null || !payDate.plusHours(1).isBefore(LocalDateTime.now());
	}
}
