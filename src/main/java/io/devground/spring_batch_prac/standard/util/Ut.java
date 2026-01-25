package io.devground.spring_batch_prac.standard.util;

import org.springframework.util.StringUtils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Ut {

	public static class str {
		public static String lcfirst(String str) {
			if (!StringUtils.hasText(str)) {
				return str;
			}

			return str.substring(0, 1).toLowerCase() + str.substring(1);
		}
	}
}
