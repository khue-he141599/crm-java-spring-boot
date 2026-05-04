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

   private String secret;
   private String issuer = "crm-api";
   private Integer accessTokenTtlMinutes = 15;
   private Integer refreshTokenTtlDays = 30;
   private String accessTokenType = "access";
   private String refreshTokenType = "refresh";
}
