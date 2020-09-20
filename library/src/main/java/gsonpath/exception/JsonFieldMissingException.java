package gsonpath.exception;

public abstract class JsonFieldMissingException extends RuntimeException {
    public JsonFieldMissingException(String message) {
        super(message);
    }
}
