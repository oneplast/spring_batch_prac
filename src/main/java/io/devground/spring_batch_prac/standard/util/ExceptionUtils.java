package io.devground.spring_batch_prac.standard.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionUtils {

	public static String toString(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String stackTrace = sw.toString();

		StringBuilder details = new StringBuilder();

		details.append("Exception Message: ").append(e.getMessage()).append("\n");

		Throwable cause = e.getCause();
		if (!Objects.isNull(cause)) {
			details.append("Caused by: ").append(cause).append("\n");
		}

		details.append("Stack Trace:\n").append(stackTrace);

		return details.toString();
	}
}
