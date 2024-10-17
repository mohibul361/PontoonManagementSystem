package com.biwta.pontoon.exceptionHandler;

/**
 * @author nasimkabir
 * ৮/৮/২৩
 */
public class FieldValidationError {
    private String field;
    private String message;

    public FieldValidationError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public FieldValidationError() {
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}
