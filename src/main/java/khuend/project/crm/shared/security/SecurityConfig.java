package khuend.project.crm.shared.security;

import java.nio.charset.StandardCharsets;

import javax.crypto.spec.SecretKeySpec;

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

      private static final String HMAC_ALGORITHM = "HmacSHA256";

      private final JwtProperties jwtProperties;
      private final ServiceKeyAuthFilter serviceKeyAuthFilter;

      public SecurityConfig(JwtProperties jwtProperties, ServiceKeyAuthFilter serviceKeyAuthFilter) {
            this.jwtProperties = jwtProperties;
            this.serviceKeyAuthFilter = serviceKeyAuthFilter;
      }

      @Bean
      PasswordEncoder passwordEncoder() {
            return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
      }

      @Bean
      SecurityFilterChain springSecurityFilterChain(HttpSecurity http) throws Exception {
            return http
                        .csrf(AbstractHttpConfigurer::disable)
                        .addFilterBefore(serviceKeyAuthFilter, UsernamePasswordAuthenticationFilter.class)
                        .authorizeHttpRequests(auth -> auth
                                    .requestMatchers("/api/users/signin", "/graphql", "/graphql/**", "/actuator/**")
                                    .permitAll()
                                    .anyRequest().authenticated())
                        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                        .httpBasic(AbstractHttpConfigurer::disable)
                        .formLogin(AbstractHttpConfigurer::disable)
                        .build();
      }

      @Bean
      JwtDecoder jwtDecoder() {
            byte[] secretBytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
            SecretKeySpec secretKey = new SecretKeySpec(secretBytes, HMAC_ALGORITHM);

            NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKey).build();

            OAuth2TokenValidator<Jwt> issuerValidator = JwtValidators
                        .createDefaultWithIssuer(jwtProperties.getIssuer());
            OAuth2TokenValidator<Jwt> accessTypeValidator = jwt -> {
                  String type = jwt.getClaimAsString("type");
                  if (jwtProperties.getAccessTokenType().equals(type)) {
                        return OAuth2TokenValidatorResult.success();
                  }
                  OAuth2Error error = new OAuth2Error("invalid_token",
                              "Token type must be " + jwtProperties.getAccessTokenType(), null);
                  return OAuth2TokenValidatorResult.failure(error);
            };

            decoder.setJwtValidator(token -> {
                  OAuth2TokenValidatorResult issuerResult = issuerValidator.validate(token);
                  if (issuerResult.hasErrors()) {
                        return issuerResult;
                  }
                  return accessTypeValidator.validate(token);
            });

            return decoder;
      }
}
