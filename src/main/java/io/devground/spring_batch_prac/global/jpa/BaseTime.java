package io.devground.spring_batch_prac.global.jpa;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;

@Getter
@MappedSuperclass
@ToString(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTime extends BaseEntity {

	@CreatedDate
	private LocalDateTime createDate;
	@LastModifiedDate
	private LocalDateTime modifyDate;
}
