package io.devground.spring_batch_prac.global.rsdata;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class RsData<T> {

	private final int resultCode;
	private final String msg;
	private final T data;

	public static <T> RsData<T> of(int resultCode, String msg, T data) {

		return new RsData<>(resultCode, msg, data);
	}

	public static <T> RsData<T> of(int resultCode, String msg) {

		return of(resultCode, msg, null);
	}

	@JsonIgnore
	public boolean isSuccess() {
		return resultCode >= 200 && resultCode < 400;
	}

	@JsonIgnore
	public boolean isFail() {
		return !isSuccess();
	}

	public RsData<T> of(T data) {
		return of(resultCode, msg, data);
	}
}
