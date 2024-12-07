package ru.ivanems.logger.util;

public class EnumUtils {

    public static boolean isValidEnum(Class<?> enumClass, String value) {
        try {
            enumClass.getMethod(value);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

}
