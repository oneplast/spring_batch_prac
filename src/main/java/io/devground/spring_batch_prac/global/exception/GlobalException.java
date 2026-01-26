package io.devground.spring_batch_prac.global.exception;

import io.devground.spring_batch_prac.global.rsdata.RsData;
import lombok.Getter;

public class GlobalException extends RuntimeException {

	@Getter
	private RsData<?> rsData;

	public GlobalException(int resultCode, String msg) {
		super(resultCode + ": " + msg);

		rsData = RsData.of(resultCode, msg);
	}
}
