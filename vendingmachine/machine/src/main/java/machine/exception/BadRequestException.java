package machine.exception;

public class BadRequestException extends RuntimeException {

    private final String errorCode;

    public BadRequestException(String message) {
        super(message);
        this.errorCode = ErrorCode.BAD_REQUEST.name();
    }

    public BadRequestException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BadRequestException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode.name();
    }

    public BadRequestException(BadRequestException.Message message, ErrorCode errorCode) {
        this(message.getFormatMessage(), errorCode);
    }

    public BadRequestException(String message, ErrorCode errorCode, Throwable e) {
        super(message, e);
        this.errorCode = errorCode.name();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public enum Message {
        PRODUCT_NOT_FOUND("Product with id %s not found.");

        final String msg;

        Message(String msg) {
            this.msg = msg;
        }

        public String getFormatMessage() {
            return msg;
        }

        public String with(Object... formatParams) {
            return String.format(msg, formatParams);
        }
    }
}