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

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter xác thực service-to-service qua header X-Service-Key.
 * Chạy trước BearerTokenAuthenticationFilter.
 * Nếu service key hợp lệ → set Authentication với ROLE_SERVICE vào
 * SecurityContext.
 * Nếu request đã có Authentication (JWT) → bỏ qua.
 */
@Component
public class ServiceKeyAuthFilter extends OncePerRequestFilter {

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
            filterChain.doFilter(request, response);
            return;
        }

        String serviceKey = request.getHeader(SERVICE_KEY_HEADER);
        if (StringUtils.hasText(serviceKey) && isValidServiceKey(serviceKey)) {
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    "service",
                    null,
                    List.of(new SimpleGrantedAuthority(ROLE_SERVICE)));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isValidServiceKey(String key) {
        return serviceKeyProperties.getServiceKeys().contains(key);
    }
}
