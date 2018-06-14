package com.github.app.spi.utils;

import org.apache.commons.validator.*;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ValidatorTest {
    private ValidatorResources validatorResources;
    private static final String FORM = "genericForm";

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

        Validator validator = new Validator(validatorResources, FORM);
        validator.setParameter(Validator.BEAN_PARAM, values);
        ValidatorResults results = validator.validate();
        ValidatorResult result = results.getValidatorResult("email");

        System.out.println(result.getResult("email"));
        Set<String> actions = validatorResources.getValidatorActions().keySet();
        System.out.println(Arrays.toString(actions.toArray()));
        System.out.println(validatorResources.getValidatorAction("email").getMsg());
    }

    public static void main(String[] args) throws ValidatorException {
        new ValidatorTest().testEmail();
    }
}
