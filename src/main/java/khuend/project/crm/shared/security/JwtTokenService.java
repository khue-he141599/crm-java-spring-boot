package khuend.project.crm.shared.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import khuend.project.crm.model.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {

   private final JwtProperties jwtProperties;
   private final SecretKey secretKey;

   public JwtTokenService(JwtProperties jwtProperties) {
      this.jwtProperties = jwtProperties;
      String secret = jwtProperties.getSecret();

      if (secret == null || secret.length() < 32) {
         throw new IllegalStateException("JWT secret must be at least 32 characters");
      }

      this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
   }

   public String generateAccessToken(UserEntity user) {
      Instant now = Instant.now();
      Instant expiresAt = now.plus(jwtProperties.getAccessTokenTtlMinutes(), ChronoUnit.MINUTES);

      return Jwts.builder()
            .subject(toSubject(user.getId()))
            .issuer(jwtProperties.getIssuer())
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiresAt))
            .claim("type", "access")
            .claim("username", user.getUsername())
            .signWith(secretKey)
            .compact();
   }

   public String generateRefreshToken(UserEntity user) {
      Instant now = Instant.now();
      Instant expiresAt = now.plus(jwtProperties.getRefreshTokenTtlDays(), ChronoUnit.DAYS);

      return Jwts.builder()
            .subject(toSubject(user.getId()))
            .issuer(jwtProperties.getIssuer())
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiresAt))
            .id(UUID.randomUUID().toString())
            .claim("type", "refresh")
            .claim("username", user.getUsername())
            .signWith(secretKey)
            .compact();
   }

   private String toSubject(UUID userId) {
      if (userId == null) {
         throw new IllegalArgumentException("User id must not be null when generating token");
      }
      return userId.toString();
   }
}
