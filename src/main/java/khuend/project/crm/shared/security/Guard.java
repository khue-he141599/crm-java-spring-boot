package khuend.project.crm.shared.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Khai báo loại xác thực cho method hoặc class.
 *
 * Ví dụ:
 * @Guard(AuthType.JWT) - chỉ Bearer token
 * @Guard(AuthType.SERVICE_KEY) - chỉ X-Service-Key
 * @Guard(AuthType.ANY) - cả hai
 *
 * Gắn lên class = áp dụng cho toàn bộ method trong class.
 * Gắn lên method = override setting của class.
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Guard {
    AuthType value() default AuthType.JWT;
}
