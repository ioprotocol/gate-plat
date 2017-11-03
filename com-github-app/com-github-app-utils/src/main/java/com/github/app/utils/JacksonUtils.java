package com.github.app.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public final class JacksonUtils {
    // ObjectMapper 只提供json到对象的互相转换，因此线程安全
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper()
                .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)            //允许单引号
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)     //允许没有引号的字段名
                .configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true)   //允许ASCII值小于32的控制字符
                .configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false)       //忽略map中的null属性
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)       //忽略map中的null属性
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)//json字符串中有而对象中没有的属性忽略掉
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);           //忽略bean中的null属性
    }

    private JacksonUtils() {
    }

    /**
     * json字符串到对象的转换
     *
     * @param jsonStr Json对象字符串
     * @return entity
     * @throws NullPointerException
     */
    public static <T> T json2Object(String jsonStr, Class<T> valueType) throws Exception {

        try {
            return objectMapper.readValue(jsonStr, valueType);
        } catch (Exception e) {
            throw new Exception("Jackson.json2Object error.", e);
        }
    }

    /**
     * @param json
     * @param valueType
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T json2Object(byte[] json, Class<T> valueType) throws Exception {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (Exception e) {
            throw new Exception("Jackson.json2Object error.", e);
        }
    }

    /**
     * 对象到json字符串的转换
     *
     * @param obj entity
     * @return jsonStr
     * @throws NullPointerException
     */
    public static String object2JsonStr(Object obj) throws Exception {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new Exception("Jackson.object2JsonStr error.", e);
        }
    }

    /**
     * @param obj
     * @return
     * @throws Exception
     */
    public static byte[] object2JsonBuf(Object obj) throws Exception {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (Exception e) {
            throw new Exception("Jackson.object2JsonStr error.", e);
        }
    }
}

