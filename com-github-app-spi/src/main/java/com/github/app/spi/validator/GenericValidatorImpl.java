package com.github.app.spi.validator;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.GenericValidator;
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

    /**
     *
     * @param bean
     * @param field
     * @return
     */
    public static boolean validateRegex(Object bean, Field field) {
        String value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        return GenericValidator.matchRegexp(value, field.getVarValue("regex"));
    }

    /**
     *
     * @param bean
     * @param field
     * @return
     */
    public static boolean validateUrl(Object bean, Field field) {
        String value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        return GenericValidator.isUrl(value);
    }

    /**
     *
     * @param bean
     * @param field
     * @return
     */
    public static boolean validateMobile(Object bean, Field field) {
        String value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        return MobileValidator.getInstance().isValid(value);
    }
    /**
     *
     * @param bean
     * @param field
     * @return
     */
    public static boolean validateNumberRange(Object bean, Field field) {
        String value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        String varMax = field.getVarValue("max");
        String varMin = field.getVarValue("min");

        if (StringUtils.isEmpty(varMax)) {
            return GenericValidator.minValue(Integer.parseInt(value), Integer.parseInt(varMin));
        }

        if (StringUtils.isEmpty(varMin)) {
            return GenericValidator.maxValue(Integer.parseInt(value), Integer.parseInt(varMax));
        }

        return GenericValidator.isInRange(Integer.valueOf(value), Integer.parseInt(varMin), Integer.parseInt(varMax));
    }

    public static boolean validateDate(Object bean, Field field) {
        String value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        String pattern = field.getVarValue("pattern");
        return GenericValidator.isDate(value, pattern, true);
    }

    public static boolean validateStringLength(Object bean, Field field) {
        String value = ValidatorUtils.getValueAsString(bean, field.getProperty());
        String varMax = field.getVarValue("max");
        String varMin = field.getVarValue("min");
        if (StringUtils.isEmpty(varMax)) {
            return GenericValidator.minLength(value, Integer.parseInt(varMin));
        }

        if (StringUtils.isEmpty(varMin)) {
            return GenericValidator.minLength(value, Integer.parseInt(varMax));
        }
        return GenericValidator.minLength(value, Integer.parseInt(varMin))
                && GenericValidator.minLength(value, Integer.parseInt(varMax));
    }
}
