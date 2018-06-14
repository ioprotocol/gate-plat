package com.github.app.spi.utils;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.util.ValidatorUtils;

/**
 * Contains validation methods for different unit tests.
 *
 * @version $Revision$
 */
public class GenericValidatorImpl {

    /**
     * Checks if the field is required.
     *
     * @return boolean If the field isn't <code>null</code> and
     * has a length greater than zero, <code>true</code> is returned.
     * Otherwise <code>false</code>.
     */
    public static boolean validateRequired(Object bean, Field field) {
        String value = ValidatorUtils.getValueAsString(bean, field.getProperty());

        return !GenericValidator.isBlankOrNull(value);
    }

    /**
     * Checks if the field can be successfully converted to a <code>byte</code>.
     *
     * @param bean  The value validation is being performed on.
     * @param field the field to use
     * @return boolean        If the field can be successfully converted
     * to a <code>byte</code> <code>true</code> is returned.
     * Otherwise <code>false</code>.
     */
    public static boolean validateByte(Object bean, Field field) {
        String value = ValidatorUtils.getValueAsString(bean, field.getProperty());

        return GenericValidator.isByte(value);
    }

    /**
     * Checks if the field can be successfully converted to a <code>short</code>.
     *
     * @param bean  The value validation is being performed on.
     * @param field the field to use
     * @return boolean        If the field can be successfully converted
     * to a <code>short</code> <code>true</code> is returned.
     * Otherwise <code>false</code>.
     */
    public static boolean validateShort(Object bean, Field field) {
        String value = ValidatorUtils.getValueAsString(bean, field.getProperty());

        return GenericValidator.isShort(value);
    }

    /**
     * Checks if the field can be successfully converted to a <code>int</code>.
     *
     * @param bean  The value validation is being performed on.
     * @param field the field to use
     * @return boolean        If the field can be successfully converted
     * to a <code>int</code> <code>true</code> is returned.
     * Otherwise <code>false</code>.
     */
    public static boolean validateInt(Object bean, Field field) {
        String value = ValidatorUtils.getValueAsString(bean, field.getProperty());

        return GenericValidator.isInt(value);
    }

    /**
     * Checks if field is positive assuming it is an integer
     *
     * @param bean  The value validation is being performed on.
     * @param field Description of the field to be evaluated
     * @return boolean     If the integer field is greater than zero, returns
     * true, otherwise returns false.
     */
    public static boolean validatePositive(Object bean, Field field) {
        String value = ValidatorUtils.getValueAsString(bean, field.getProperty());

        return GenericTypeValidator.formatInt(value).intValue() > 0;
    }

    /**
     * Checks if the field can be successfully converted to a <code>long</code>.
     *
     * @param bean  The value validation is being performed on.
     * @param field the field to use
     * @return boolean        If the field can be successfully converted
     * to a <code>long</code> <code>true</code> is returned.
     * Otherwise <code>false</code>.
     */
    public static boolean validateLong(Object bean, Field field) {
        String value = ValidatorUtils.getValueAsString(bean, field.getProperty());

        return GenericValidator.isLong(value);
    }

    /**
     * Checks if the field can be successfully converted to a <code>float</code>.
     *
     * @param bean  The value validation is being performed on.
     * @param field the field to use
     * @return boolean        If the field can be successfully converted
     * to a <code>float</code> <code>true</code> is returned.
     * Otherwise <code>false</code>.
     */
    public static boolean validateFloat(Object bean, Field field) {
        String value = ValidatorUtils.getValueAsString(bean, field.getProperty());

        return GenericValidator.isFloat(value);
    }

    /**
     * Checks if the field can be successfully converted to a <code>double</code>.
     *
     * @param bean  The value validation is being performed on.
     * @param field the field to use
     * @return boolean        If the field can be successfully converted
     * to a <code>double</code> <code>true</code> is returned.
     * Otherwise <code>false</code>.
     */
    public static boolean validateDouble(Object bean, Field field) {
        String value = ValidatorUtils.getValueAsString(bean, field.getProperty());

        return GenericValidator.isDouble(value);
    }

    /**
     * Checks if the field is an e-mail address.
     *
     * @param bean  The value validation is being performed on.
     * @param field the field to use
     * @return boolean        If the field is an e-mail address
     * <code>true</code> is returned.
     * Otherwise <code>false</code>.
     */
    public static boolean validateEmail(Object bean, Field field) {
        String value = ValidatorUtils.getValueAsString(bean, field.getProperty());

        return GenericValidator.isEmail(value);
    }

    public static boolean validateText(Object bean, Field field) {
        String value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        return false;
    }

}
