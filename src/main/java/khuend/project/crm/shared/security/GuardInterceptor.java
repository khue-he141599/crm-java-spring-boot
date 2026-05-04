package khuend.project.crm.shared.security;

import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import khuend.project.crm.shared.exception.AppException;
import khuend.project.crm.shared.exception.ErrorCode;

/**
 * Interceptor chạy SAU khi Spring Security đã populate SecurityContext.
 * Đọc @Guard trên method/class và kiểm tra loại authentication phù hợp.
 * 
 * @PublicEndpoint = bỏ qua kiểm tra.
 */
@Component
public class GuardInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) {

        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // @PublicEndpoint trên method → bypass hoàn toàn
        if (handlerMethod.hasMethodAnnotation(PublicEndpoint.class)) {
            return true;
        }

        // Tìm @Guard: ưu tiên method trước, rồi đến class
        Guard guard = handlerMethod.getMethodAnnotation(Guard.class);
        if (guard == null) {
            guard = handlerMethod.getBeanType().getAnnotation(Guard.class);
        }

        // Không có @Guard → cho qua (filter chain đã xử lý anyRequest)
        if (guard == null) {
            return true;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AppException(ErrorCode.TOKEN_MISSING);
        }

        AuthType required = guard.value();
        boolean isJwt = auth.getPrincipal() instanceof Jwt;
        boolean isServiceKey = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(ServiceKeyAuthFilter.ROLE_SERVICE::equals);

        boolean allowed = switch (required) {
            case JWT -> isJwt;
            case SERVICE_KEY -> isServiceKey;
            case ANY -> isJwt || isServiceKey;
        };

        if (!allowed) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        return true;
    }
}
