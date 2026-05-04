package khuend.project.crm.shared.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

      @Bean
      public PasswordEncoder passwordEncoder() {
            return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
      }

      @Bean
      public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
            return http
                        .csrf(ServerHttpSecurity.CsrfSpec::disable)
                        .authorizeExchange(exchange -> exchange
                                    .pathMatchers("/graphql", "/graphql/**", "/api/**", "/actuator/**").permitAll()
                                    .anyExchange().authenticated())
                        .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                        .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                        .build();
      }
}
