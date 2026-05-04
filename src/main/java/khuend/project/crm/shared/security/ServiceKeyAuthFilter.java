package khuend.project.crm.shared.security;

import java.io.IOException;
import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import khuend.project.crm.shared.exception.AppException;
import khuend.project.crm.shared.exception.ErrorCode;

/**
 * Filter xác thực service-to-service qua header X-Service-Key.
 * Chạy trước BearerTokenAuthenticationFilter.
 * Nếu service key hợp lệ → set Authentication với ROLE_SERVICE vào
 * SecurityContext.
 * Nếu request đã có Authentication (JWT) → bỏ qua.
 */
@Component
public class ServiceKeyAuthFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private static final Logger log = LoggerFactory.getLogger(ServiceKeyAuthFilter.class);

    public static final String SERVICE_KEY_HEADER = "X-Service-Key";
    public static final String ROLE_SERVICE = "ROLE_SERVICE";

    private final ServiceKeyProperties serviceKeyProperties;

    public ServiceKeyAuthFilter(ServiceKeyProperties serviceKeyProperties) {
        this.serviceKeyProperties = serviceKeyProperties;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Bỏ qua nếu đã authenticated (ví dụ: JWT đã được xử lý)
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
                log.debug("[AUTH_FLOW][REQ-01][SERVICE_KEY_FILTER] Skip because context already authenticated path={}",
                    request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        // Nếu có service key header thì bắt buộc xác thực theo service key.
        String serviceKey = request.getHeader(SERVICE_KEY_HEADER);
        if (StringUtils.hasText(serviceKey)) {
            if (!isValidServiceKey(serviceKey)) {
                log.warn("[AUTH_FLOW][REQ-01][SERVICE_KEY_FILTER] Invalid service key path={}", request.getRequestURI());
                throw new AppException(ErrorCode.TOKEN_INVALID);
            }

            log.info("[AUTH_FLOW][REQ-01][SERVICE_KEY_FILTER] Service key authenticated path={}", request.getRequestURI());
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    "service",
                    null,
                    List.of(new SimpleGrantedAuthority(ROLE_SERVICE)));
            SecurityContextHolder.getContext().setAuthentication(auth);

            filterChain.doFilter(request, response);
            return;
        }

        // Không có service key thì chuyển sang JWT (Bearer token).
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(authorization) && authorization.startsWith(BEARER_PREFIX)) {
            log.debug("[AUTH_FLOW][REQ-01][SERVICE_KEY_FILTER] Bearer token detected, skip service-key path={}",
                request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        log.warn("[AUTH_FLOW][REQ-01][SERVICE_KEY_FILTER] Missing credentials (no service key, no bearer token) path={}",
                request.getRequestURI());
        throw new AppException(ErrorCode.TOKEN_MISSING);
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getServletPath();
        return "/api/users/signin".equals(path)
                || "/graphql".equals(path)
                || path.startsWith("/graphql/")
                || "/actuator".equals(path)
                || path.startsWith("/actuator/");
    }

    private boolean isValidServiceKey(String key) {
        return serviceKeyProperties.getServiceKeys().contains(key);
    }
}
