package com.obervatorio_pedagogico.backend.infrastructure.utils.converters;

public class NumberConverters {
    public static Integer stringToInteger(String input) {
        input = input.replaceAll(",", ".");
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException exception) {
            return null;
        }
    }

    public static Float stringToFloat(String input) {
        input = input.replaceAll(",", ".");
        try {
            return Float.parseFloat(input);
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}
