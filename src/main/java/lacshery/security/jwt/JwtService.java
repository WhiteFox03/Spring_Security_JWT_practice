package lacshery.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lacshery.dto.jwtToken.JwtAuthenticationDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    private static final Logger LOGGER = LogManager.getLogger(JwtService.class);
    @Value("779725319047db691244a73e800bd1fa08c848ea4df01b51fe10601bf3b73c11")
    private String JwtSecret;

    public JwtAuthenticationDto generatorAuthToken(String email) {
        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generatorJwtToken(email));
        jwtDto.setRefreshToken(generatorRefreshJwtToken(email));
        return jwtDto;
    }

    public JwtAuthenticationDto refreshBaseToken(String email, String refreshToken) {
        JwtAuthenticationDto jwtDto = new JwtAuthenticationDto();
        jwtDto.setToken(generatorJwtToken(email));
        jwtDto.setRefreshToken(refreshToken);
        return jwtDto;
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSingInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSingInKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            LOGGER.error("Expired JwtException", e);
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Unsupported JwtException", e);
        } catch (MalformedJwtException e) {
            LOGGER.error("Malformed Jwt Exception", e);
        } catch (SecurityException e) {
            LOGGER.error("Security Exception", e);
        } catch (Exception e) {
            LOGGER.error("Invalid token", e);
        }
        return false;
    }

    private String generatorJwtToken(String email) {
        Date date = Date.from(Instant.now().plus(Duration.ofMinutes(5)));
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(date)
                .signWith(getSingInKey())
                .compact();
    }

    private String generatorRefreshJwtToken(String email) {
        Date date = Date.from(Instant.now().plus(Duration.ofDays(1)));
        return Jwts.builder()
                .setSubject(email)
                .setExpiration(date)
                .signWith(getSingInKey())
                .compact();
    }

    private SecretKey getSingInKey() {
        byte[] keyBates = Decoders.BASE64.decode(JwtSecret);
        return Keys.hmacShaKeyFor(keyBates);
    }
}