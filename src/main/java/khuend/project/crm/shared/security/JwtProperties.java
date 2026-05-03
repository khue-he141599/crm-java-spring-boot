package khuend.project.crm.shared.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.security.jwt")
public class JwtProperties {

   // Using @ConfigurationProperties to bind properties from application.properties or environment variables
   private String secret;
   
   // Using System.getProperty with defaults to allow overriding via environment variables 
   private String issuer = System.getProperty("JWT_ISSUER", "crm-api");
   private Integer accessTokenTtlMinutes = Integer.parseInt(System.getProperty("JWT_ACCESS_TOKEN_TTL_MINUTES", "15"));
   private Integer refreshTokenTtlDays = Integer.parseInt(System.getProperty("JWT_REFRESH_TOKEN_TTL_DAYS", "30"));
}
