package io.devground.spring_batch_prac.domain.member.member.entity;

import java.util.ArrayList;
import java.util.List;

import io.devground.spring_batch_prac.domain.book.book.entity.Book;
import io.devground.spring_batch_prac.domain.member.myBook.entity.MyBook;
import io.devground.spring_batch_prac.domain.product.product.entity.Product;
import io.devground.spring_batch_prac.global.jpa.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
public class Member extends BaseEntity {

	private String username;
	private String password;
	private long restCash;

	@Builder.Default
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MyBook> myBooks = new ArrayList<>();

	public void addMyBook(Book book) {
		MyBook myBook = MyBook.builder()
			.owner(this)
			.book(book)
			.build();

		myBooks.add(myBook);
	}

	public void removeMyBook(Book book) {
		myBooks.removeIf(myBook -> myBook.getBook().equals(book));
	}

	public boolean hasBook(Book book) {
		return myBooks.stream()
			.anyMatch(myBook -> myBook.getBook().equals(book));
	}

	public boolean has(Product product) {
		return switch (product.getRelTypeCode()) {
			case "book" -> hasBook(product.getBook());
			default -> false;
		};
	}
}
