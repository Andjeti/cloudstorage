package ru.netology.cloudstorage.config;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.exception.JwtAuthenticationException;
import ru.netology.cloudstorage.model.CloudUser;

import java.util.Date;
import static java.lang.String.format;
import static java.security.cert.CertPathValidatorException.BasicReason.INVALID_SIGNATURE;

@Component
public class JwtTokenUtil {

    private final String jwtSecret = "zdtlD3JK56m6wTTgsNFhqzjqP";
    private final String jwtIssuer = "netology.ru";

    public String generateAccessToken(CloudUser cloudUser) {
        return Jwts.builder()
                .setSubject(format("%s,%s", cloudUser.getId(), cloudUser.getUsername()))
                .setIssuer(jwtIssuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 1 week
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject().split(",")[0];
    }

    public String getUsername(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject().split(",")[1];
    }

    public Date getExpirationDate(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        // сократила кучу эксепшнов
//        } catch (SignatureException ex) {
//            logger.error("Invalid JWT signature - {}", ex.getMessage());
//        } catch (MalformedJwtException ex) {
//            logger.error("Invalid JWT token - {}", ex.getMessage());
//        } catch (ExpiredJwtException ex) {
//            logger.error("Expired JWT token - {}", ex.getMessage());
//        } catch (UnsupportedJwtException ex) {
//            logger.error("Unsupported JWT token - {}", ex.getMessage());
//        } catch (IllegalArgumentException ex) {
//            logger.error("JWT claims string is empty - {}", ex.getMessage());
        } catch (JwtException | IllegalArgumentException ex) {
                throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
    }
}
