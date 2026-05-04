package khuend.project.crm.shared.exception;

import java.time.Instant;

public record ErrorResponse(
        int status,
        String code,
        String error,
        String message,
        String path,
        Instant timestamp) {

    /** Dùng cho AppException (có ErrorCode) */
    public static ErrorResponse of(ErrorCode errorCode, String message, String path) {
        return new ErrorResponse(
                errorCode.getHttpStatus().value(),
                errorCode.getCode(),
                errorCode.getHttpStatus().getReasonPhrase(),
                message,
                path,
                Instant.now());
    }

    /** Dùng cho các exception thông thường (không có ErrorCode) */
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(status, null, error, message, path, Instant.now());
    }
}
