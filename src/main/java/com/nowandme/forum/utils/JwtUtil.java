package com.nowandme.forum.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.expiryTimeInHours}")
    private Integer EXPIRY_TIME_HOUR;

    private String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     *
     * @param userId
     * @return
     *  Return JWT for usedId
     */
    public String generateToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userId);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*(EXPIRY_TIME_HOUR*1L)))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    /**
     *
     * @param token
     * @return
     *  Return if the JWT is valid
     */
    public Boolean validateToken(String token) {
        extractUsername(token);
        return !isTokenExpired(token);
    }

    /**
     *
     * @param currentToken
     * @return
     *  Return new JWT for given JWT
     * @throws JwtException
     */
    public String refreshToken(String currentToken) throws JwtException {
        if(validateToken(currentToken)) {
            String userId = extractUsername(currentToken);
            return generateToken(userId);
        }
        else
            throw new JwtException("Invalid token provided");
    }
}
