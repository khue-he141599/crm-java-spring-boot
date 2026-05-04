package khuend.project.crm.shared.exception;

import org.springframework.http.HttpStatus;

/**
 * Bộ mã lỗi toàn hệ thống.
 * Format mã: [DOMAIN]_[NNN]
 * USR - User
 * AUTH - Authentication / Authorization
 * DEP - Department
 * CMN - Common / Generic
 */
public enum ErrorCode {

    // ----------------------------------------------------------------
    // AUTH
    // ----------------------------------------------------------------
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUTH_001", "Invalid username or password"),
    ACCOUNT_NOT_ACTIVE(HttpStatus.FORBIDDEN, "AUTH_002", "User account is not active"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "AUTH_003", "Access denied"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_004", "Token has expired"),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "AUTH_005", "Token is invalid"),
    TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "AUTH_006", "Authorization token is missing"),

    // ----------------------------------------------------------------
    // USER
    // ----------------------------------------------------------------
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USR_001", "User not found"),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "USR_002", "Username already exists"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "USR_003", "Email already exists"),
    PHONE_ALREADY_EXISTS(HttpStatus.CONFLICT, "USR_004", "Phone number already exists"),

    // ----------------------------------------------------------------
    // DEPARTMENT
    // ----------------------------------------------------------------
    DEPARTMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "DEP_001", "Department not found"),

    // ----------------------------------------------------------------
    // COMMON
    // ----------------------------------------------------------------
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "CMN_001", "Validation failed"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "CMN_002", "Resource not found"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "CMN_500", "Internal server error");

    // ----------------------------------------------------------------

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
