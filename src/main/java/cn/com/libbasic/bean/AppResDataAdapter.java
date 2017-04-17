package cn.com.libbasic.bean;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class AppResDataAdapter implements JsonSerializer<AppResData>, JsonDeserializer<AppResData> {
	private Type mResultType;

	public AppResDataAdapter(Type resultType) {
		mResultType = resultType;
	}

	@Override
	public AppResData deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
		Gson gson = new Gson();
		return gson.fromJson(arg0, mResultType);
	}

	@Override
	public JsonElement serialize(AppResData arg0, Type arg1, JsonSerializationContext arg2) {
		Gson gson = new Gson();
		return gson.toJsonTree(arg0);
	}
}
