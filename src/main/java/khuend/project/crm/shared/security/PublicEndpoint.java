package khuend.project.crm.shared.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

/**
 * Đánh dấu method hoặc class không yêu cầu xác thực.
 * Dùng kết hợp với @Guard ở cấp class để cho phép bypass một số API nhất định.
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("permitAll()")
public @interface PublicEndpoint {
}
