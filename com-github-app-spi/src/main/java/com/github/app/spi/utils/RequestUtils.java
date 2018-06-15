package com.github.app.spi.utils;

import io.vertx.core.MultiMap;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.routines.IntegerValidator;
import org.apache.commons.validator.routines.LongValidator;
import org.springframework.util.StringUtils;

public class RequestUtils {

    public static Integer getInteger(MultiMap map, String key) {
        if (!IntegerValidator.getInstance().isValid(map.get(key))) {
            return null;
        }
        if (!StringUtils.isEmpty(map.get(key))) {
            return Integer.valueOf(map.get(key));
        }
        return null;
    }

    public static Long getLong(MultiMap map, String key) {
        if (!LongValidator.getInstance().isValid(map.get(key))) {
            return null;
        }
        if (!StringUtils.isEmpty(map.get(key))) {
            return Long.valueOf(map.get(key));
        }
        return null;
    }

    public static Boolean getBoolean(MultiMap map, String key) {
        if (!StringUtils.isEmpty(map.get(key))) {
            return Boolean.valueOf(map.get(key));
        }
        return null;
    }
}
