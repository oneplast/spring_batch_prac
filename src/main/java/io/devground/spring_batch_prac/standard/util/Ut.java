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

	public static class url {
		public static String modifyQueryParam(String url, String paramName, String paramValue) {
			url = deleteQueryParam(url, paramName);
			url = addQueryParam(url, paramName, paramValue);

			return url;
		}

		public static String addQueryParam(String url, String paramName, String paramValue) {
			if (!url.contains("?")) {
				url += "?";
			}

			if (!url.endsWith("?") && !url.endsWith("&")) {
				url += "&";
			}

			url += paramName + "=" + paramValue;

			return url;
		}

		public static String deleteQueryParam(String url, String paramName) {
			int startPoint = url.indexOf(paramName + "=");

			if (startPoint == -1) {
				return url;
			}

			int endPoint = url.substring(startPoint).indexOf("&");

			if (endPoint == -1) {
				return url.substring(0, startPoint - 1);
			}

			String urlAfter = url.substring(startPoint + endPoint + 1);

			return url.substring(0, startPoint) + urlAfter;
		}
	}
}
