package io.devground.spring_batch_prac.global.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.devground.spring_batch_prac.global.rq.Rq;
import lombok.RequiredArgsConstructor;

@ControllerAdvice(annotations = Controller.class)
@RequiredArgsConstructor
public class GlobalCommonExceptionHandler {

	private final Rq rq;

	@ExceptionHandler(GlobalException.class)
	public String handle(GlobalException ex) {
		return rq.historyBack(ex);
	}
}
