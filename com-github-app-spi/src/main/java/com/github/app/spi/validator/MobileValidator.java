package com.github.app.spi.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MobileValidator {
    private static final MobileValidator VALIDATOR = new MobileValidator();

    private static final String REGEX = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";

    private static final Pattern PATTERN = Pattern.compile(REGEX);

    public static MobileValidator getInstance() {
        return VALIDATOR;
    }

    public boolean isValid(String mobile) {
        if (mobile == null) {
            return false;
        }

        // Check the whole email address structure
        Matcher matcher = PATTERN.matcher(mobile);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }
}
