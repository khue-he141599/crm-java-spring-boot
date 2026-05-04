package khuend.project.crm.shared.exception;

/**
 * Custom runtime exception dùng ErrorCode để thống nhất mã lỗi toàn hệ thống.
 *
 * Cách dùng:
 * throw new AppException(ErrorCode.USER_NOT_FOUND);
 * throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
 */
public class AppException extends RuntimeException {

    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public AppException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
