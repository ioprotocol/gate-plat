package com.foton.buffer.protocol.utils;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Iterator;
import java.util.Map;

public class JsonFlatUtils {
    private JsonFlatUtils() {}

    public static JsonObject toFlat(JsonObject jsonObject) {
        return _toFlat(jsonObject, "", new JsonObject());
    }

    private static JsonObject _toFlat(JsonObject obj, String keyPrefix, JsonObject flatObj) {
        Iterator<Map.Entry<String, Object>> iterator = obj.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            Object value = entry.getValue();
            if(value instanceof JsonObject) {
                _toFlat((JsonObject)value, keyPrefix + entry.getKey() + ".", flatObj);
            } else if(value instanceof JsonArray) {
                JsonArray array = (JsonArray) value;
                if(array.size() > 0) {
                    if(array.getValue(0) instanceof JsonObject) {
                        for(int i = 0; i < array.size(); i++) {
                            _toFlat(array.getJsonObject(i), keyPrefix + entry.getKey() + ".", flatObj);
                        }
                    }
                }
            } else {
                flatObj.put(keyPrefix + entry.getKey(), entry.getValue());
            }
        }
        return flatObj;
    }
}
