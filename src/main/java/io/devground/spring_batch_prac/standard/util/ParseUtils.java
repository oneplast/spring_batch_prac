package io.devground.spring_batch_prac.standard.util;

import org.springframework.util.ObjectUtils;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ParseUtils {

	public JSONObject parseToJsonObject(String body) throws ParseException {
		if (ObjectUtils.isEmpty(body)) {
			return new JSONObject();
		}

		return (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(body);
	}
}
