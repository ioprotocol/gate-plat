package com.github.app.spi.validator;

import org.apache.commons.validator.*;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ValidatorTest {
    private ValidatorResources validatorResources;
    private static final String FORM = "interceptorForm";

    public ValidatorTest() {
        try {
            validatorResources = new ValidatorResources(ValidatorTest.class.getResourceAsStream("/validator.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public void testEmail() throws ValidatorException {
        Map<String, Object> values = new HashMap<>();
        values.put("email", "xsy@123.com");
        values.put("account", "abcd.@com");
        values.put("test", "abcd.@com");

        Validator validator = new Validator(validatorResources, FORM);
        validator.setParameter(Validator.BEAN_PARAM, values);
        ValidatorResults results = validator.validate();
        ValidatorResult result1 = results.getValidatorResult("email");
        ValidatorResult result2 = results.getValidatorResult("account");
        ValidatorResult result3 = results.getValidatorResult("test");

        System.out.println(result1.getResult("email"));
        System.out.println(result2.getResult("regex"));
        System.out.println(result2.isValid("regex"));
        System.out.println(result3 == null);
    }

    public static void main(String[] args) throws ValidatorException {
        new ValidatorTest().testEmail();
    }
}
