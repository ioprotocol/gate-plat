package com.github.app.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.DataInput;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

public final class JacksonUtils {
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
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);

        SimpleModule module = new SimpleModule();
        // custom types
        module.addSerializer(JsonObject.class, new JsonObjectSerializer());
        module.addDeserializer(JsonObject.class, new JsonObjectDeserializer());
        module.addSerializer(JsonArray.class, new JsonArraySerializer());
        module.addDeserializer(JsonArray.class, new JsonArrayDeserializer());
        // he have 2 extensions: RFC-7493
        module.addSerializer(Instant.class, new InstantSerializer());
        module.addSerializer(byte[].class, new ByteArraySerializer());

        objectMapper.registerModule(module);
    }

    private JacksonUtils() {
    }

    private static class JsonObjectSerializer extends JsonSerializer<JsonObject> {
        @Override
        public void serialize(JsonObject value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeObject(value.getMap());
        }
    }

    private static class JsonObjectDeserializer extends JsonDeserializer<JsonObject> {

        @Override
        public JsonObject deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return new JsonObject(p.getCodec().readValue(p, Map.class));
        }
    }

    private static class JsonArraySerializer extends JsonSerializer<JsonArray> {
        @Override
        public void serialize(JsonArray value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeObject(value.getList());
        }
    }

    private static class JsonArrayDeserializer extends JsonDeserializer<JsonArray> {

        @Override
        public JsonArray deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return new JsonArray(p.getCodec().readValue(p, List.class));
        }
    }

    private static class InstantSerializer extends JsonSerializer<Instant> {
        @Override
        public void serialize(Instant value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(ISO_INSTANT.format(value));
        }
    }

    private static class ByteArraySerializer extends JsonSerializer<byte[]> {
        private final Base64.Encoder BASE64 = Base64.getEncoder();

        @Override
        public void serialize(byte[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
            jgen.writeString(BASE64.encodeToString(value));
        }
    }

    /**
     * json字符串到对象的转换
     *
     * @param jsonStr Json对象字符串
     * @return entity
     * @throws NullPointerException
     */
    public static <T> T deserialize(String jsonStr, Class<T> valueType) {
        try {
            return objectMapper.readValue(jsonStr, valueType);
        } catch (Exception e) {
            throw new RuntimeException("Jackson.deserialize error.", e);
        }
    }

    /**
     * 直接从Netty ByteBuf反序列化，减少内存copy
     *
     * @param byteBuf
     * @param valueType
     * @param <T>
     * @return
     */
    public static <T> T deserialize(ByteBuf byteBuf, Class<T> valueType) {
        try {
            return objectMapper.readValue((DataInput) new ByteBufInputStream(byteBuf), valueType);
        } catch (Exception e) {
            throw new RuntimeException("Jackson.deserialize error.", e);
        }
    }

    /**
     * @param jsonStr
     * @param reference
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T deserialize(String jsonStr, TypeReference<T> reference) {
        try {
            return objectMapper.readValue(jsonStr, reference);
        } catch (Exception e) {
            throw new RuntimeException("Jackson.deserialize error.", e);
        }
    }

    /**
     *
     * @param byteBuf
     * @param reference
     * @param <T>
     * @return
     */
    public static <T> T deserialize(ByteBuf byteBuf, TypeReference<T> reference) {
        try {
            return objectMapper.readValue(new ByteBufInputStream(byteBuf), reference);
        } catch (Exception e) {
            throw new RuntimeException("Jackson.deserialize error.", e);
        }
    }


    /**
     * @param json
     * @param valueType
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T deserialize(byte[] json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (Exception e) {
            throw new RuntimeException("Jackson.deserialize error.", e);
        }
    }

    /**
     * 对象到json字符串的转换
     *
     * @param obj entity
     * @return jsonStr
     * @throws NullPointerException
     */
    public static String serialize(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Jackson.serialize error.", e);
        }
    }

    /**
     * 直接json序列化为Netty的ByteBuf对象，减少内存复制
     *
     * @param obj
     * @param byteBuf
     * @return
     */
    public static ByteBuf serialize(Object obj, ByteBuf byteBuf) {
        try {
            objectMapper.writeValue((OutputStream) new ByteBufOutputStream(byteBuf), obj);
        } catch (Exception e) {
            throw new RuntimeException("Jackson.serialize error.", e);
        }
        return byteBuf;
    }

    /**
     * @param obj
     * @return
     */
    public static String serializePretty(Object obj) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Jackson.serialize error.", e);
        }
    }

    /**
     * @param obj
     * @return
     * @throws Exception
     */
    public static byte[] serialize2buf(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (Exception e) {
            throw new RuntimeException("Jackson.serialize error.", e);
        }
    }
}

