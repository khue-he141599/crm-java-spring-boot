package khuend.project.crm.shared.security;

import java.nio.charset.StandardCharsets;

import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

      private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

      private static final String HMAC_ALGORITHM = "HmacSHA256";

      private final JwtProperties jwtProperties;
      private final ServiceKeyAuthFilter serviceKeyAuthFilter;

      public SecurityConfig(JwtProperties jwtProperties, ServiceKeyAuthFilter serviceKeyAuthFilter) {
            this.jwtProperties = jwtProperties;
            this.serviceKeyAuthFilter = serviceKeyAuthFilter;
      }

      @Bean
      public PasswordEncoder passwordEncoder() {
            return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
      }

      @Bean
      public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            log.info("[AUTH_FLOW][BOOT-01][SECURITY_CONFIG] Configuring security filter chain issuer={} accessType={}",
                        jwtProperties.getIssuer(), jwtProperties.getAccessTokenType());
            return http
                        .csrf(AbstractHttpConfigurer::disable)
                        .addFilterBefore(serviceKeyAuthFilter, UsernamePasswordAuthenticationFilter.class)
                        .authorizeHttpRequests(auth -> auth
                                    .requestMatchers("/api/user/signin", "/graphql", "/graphql/**", "/actuator/**")
                                    .permitAll()
                                    .anyRequest().authenticated())
                        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                        .httpBasic(AbstractHttpConfigurer::disable)
                        .formLogin(AbstractHttpConfigurer::disable)
                        .build();
      }

      @Bean
      public JwtDecoder jwtDecoder() {
            byte[] secretBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKey = new SecretKeySpec(secretBytes, HMAC_ALGORITHM);

            NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKey).build();
            log.info("[AUTH_FLOW][BOOT-02][JWT_DECODER] JwtDecoder initialized issuer={} accessType={}",
                        jwtProperties.getIssuer(), jwtProperties.getAccessTokenType());

            decoder.setJwtValidator(buildJwtValidator());

            return decoder;
      }

      private OAuth2TokenValidator<Jwt> buildJwtValidator() {
            OAuth2TokenValidator<Jwt> issuerValidator = buildIssuerValidator();
            OAuth2TokenValidator<Jwt> accessTypeValidator = buildAccessTypeValidator();

            return token -> {
                  OAuth2TokenValidatorResult issuerResult = issuerValidator.validate(token);
                  if (issuerResult.hasErrors()) {
                        log.warn("[AUTH_FLOW][REQ-02][JWT_DECODER] Issuer validation failed sub={} issuer={}",
                                    token.getSubject(), token.getIssuer());
                        return issuerResult;
                  }

                  log.debug("[AUTH_FLOW][REQ-02][JWT_DECODER] Issuer validation passed sub={}", token.getSubject());
                  return accessTypeValidator.validate(token);
            };
      }

      private OAuth2TokenValidator<Jwt> buildIssuerValidator() {
            return JwtValidators.createDefaultWithIssuer(jwtProperties.getIssuer());
      }

      private OAuth2TokenValidator<Jwt> buildAccessTypeValidator() {
            return jwt -> {
                  String type = jwt.getClaimAsString("type");
                  if (jwtProperties.getAccessTokenType().equals(type)) {
                        log.debug("[AUTH_FLOW][REQ-03][JWT_DECODER] Access token type valid sub={} type={}",
                                    jwt.getSubject(), type);
                        return OAuth2TokenValidatorResult.success();
                  }

                  log.warn("[AUTH_FLOW][REQ-03][JWT_DECODER] Invalid token type sub={} expected={} actual={}",
                              jwt.getSubject(), jwtProperties.getAccessTokenType(), type);
                  OAuth2Error error = new OAuth2Error(
                              "invalid_token",
                              "Token type must be " + jwtProperties.getAccessTokenType(),
                              null);
                  return OAuth2TokenValidatorResult.failure(error);
            };
      }
}
