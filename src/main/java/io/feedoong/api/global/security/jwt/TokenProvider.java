package io.feedoong.api.global.security.jwt;

import io.feedoong.api.domain.user.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
public class TokenProvider {

    private final SecretKey SECRET_KEY;

    private static final String ISSUER = "feedoong";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int BEARER_PREFIX_LENGTH = BEARER_PREFIX.length();

    private static final long EXPIRY_AMOUNT = 1;
    private static final ChronoUnit EXPIRY_UNIT = ChronoUnit.DAYS;

    public String create(User user) {
        String subject = user.getEmail();
        Date issuedAt = new Date();
        Date expiryDate = getExpiryDate(EXPIRY_AMOUNT, EXPIRY_UNIT);

        String bearerToken = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .setSubject(subject)
                .setIssuer(ISSUER)
                .setIssuedAt(issuedAt)
                .setExpiration(expiryDate)
                .compact();

        return BEARER_PREFIX + bearerToken;
    }

    public String parseBearerToken(String bearerToken) {
        if (!isBearerToken(bearerToken)) {
            throw new BadCredentialsException("올바르지 않은 형식의 Bearer 토큰입니다.");
        }

        return bearerToken.substring(BEARER_PREFIX_LENGTH);
    }

    public void validate(String jwtToken) {
        if (isJwtTokenNull(jwtToken) || !isValid(jwtToken)) {
            throw new JwtException("유효하지 않은 jwt 토큰입니다.");
        }
    }

    public String getSubject(String jwtToken) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody()
                .getSubject();
    }

    private boolean isValid(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(jwtToken);

            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isJwtTokenNull(String jwtToken) {
        if (jwtToken == null || jwtToken.equalsIgnoreCase("null")) {
            return true;
        } else {
            return false;
        }
    }

    private Date getExpiryDate(long amount, ChronoUnit unit) {
        return Date.from(Instant.now().plus(amount, unit));
    }

    private boolean isBearerToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
            return true;
        } else {
            return false;
        }
    }
}
