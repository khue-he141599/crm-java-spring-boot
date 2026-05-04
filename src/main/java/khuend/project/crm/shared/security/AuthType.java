package khuend.project.crm.shared.security;

/**
 * Loại xác thực yêu cầu tại từng API.
 *
 * Dùng với @Guard:
 * @Guard(AuthType.JWT) - chỉ chấp nhận Bearer token
 * @Guard(AuthType.SERVICE_KEY) - chỉ chấp nhận X-Service-Key
 * @Guard(AuthType.ANY) - JWT hoặc service key đều được
 */
public enum AuthType {

    /** Chỉ chấp nhận Bearer JWT token (user đã đăng nhập). */
    JWT,

    /** Chỉ chấp nhận X-Service-Key (service-to-service). */
    SERVICE_KEY,

    /** Chấp nhận cả JWT lẫn service key. */
    ANY
}
