package gsonpath.exception;

import java.io.IOException;

public class JsonUnexpectedEnumValueException extends IOException {
    public JsonUnexpectedEnumValueException(String value, String className) {
        super("Unexpected enum value '" + value + "' for class '" + className + "'");
    }
}
