package io.devground.spring_batch_prac.domain.product.product.entity;

import io.devground.spring_batch_prac.domain.book.book.entity.Book;
import io.devground.spring_batch_prac.domain.member.member.entity.Member;
import io.devground.spring_batch_prac.global.app.AppConfig;
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
public class Product extends BaseTime {

	@ManyToOne
	private Member maker;

	private String relTypeCode;
	private long relId;

	private String name;
	private long price;

	private boolean published;

	public Book getBook() {
		return AppConfig.getEntityManager().getReference(Book.class, relId);
	}

	public boolean isBook() {
		return relTypeCode.equals("book");
	}
}
