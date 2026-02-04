package io.devground.spring_batch_prac.global.jpa;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.devground.spring_batch_prac.standard.util.Ut;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public abstract class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;

	public String getModelName() {
		return Ut.str.lcfirst(this.getClass().getSimpleName());
	}
}
