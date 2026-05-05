package khuend.project.crm.shared.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebGuardConfig implements WebMvcConfigurer {

    private final GuardInterceptor guardInterceptor;

    public WebGuardConfig(GuardInterceptor guardInterceptor) {
        this.guardInterceptor = guardInterceptor;
    }

    @SuppressWarnings("null")
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(guardInterceptor);
    }
}
