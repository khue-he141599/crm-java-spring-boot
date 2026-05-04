package khuend.project.crm.shared.security;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.security")
public class ServiceKeyProperties {

    /**
     * Danh sách service key hợp lệ cho giao tiếp service-to-service.
     * Cấu hình qua env: APP_SECURITY_SERVICE_KEYS=key1,key2
     */
    private List<String> serviceKeys = List.of();
}
