package com.heachi.auth.api.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.token.access-expiration-time}")
    private long accessExpirationTime;

    @Value("${jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;

    /*
    *   Token에서 사용자 이름 추출
    */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    /*
    *   AccessToken 생성
    */
    public String generateAccessToken(UserDetails userDetails) {
        return generateAccessToken(new HashMap<>(), userDetails, new Date(System.currentTimeMillis() + accessExpirationTime));
    }

    public String generateAccessToken(Map<String, String> extraClaims, UserDetails userDetails) {
        return generateAccessToken(extraClaims, userDetails, new Date(System.currentTimeMillis() + accessExpirationTime));
    }

    public String generateAccessToken(Map<String, String> extraClaims, UserDetails userDetails, Date expiredTime) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiredTime)
                .signWith(getSignInkey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /*
    *   RefreshToken 생성
    */
    public String generateRefreshToken(UserDetails userDetails) {
        return generateAccessToken(new HashMap<>(), userDetails, new Date(System.currentTimeMillis() + refreshExpirationTime));
    }

    public String generateRefreshToken(Map<String, String> extraClaims, UserDetails userDetails) {
        return generateRefreshToken(extraClaims, userDetails, new Date(System.currentTimeMillis() + refreshExpirationTime));
    }

    public String generateRefreshToken(Map<String, String> extraClaims, UserDetails userDetails, Date expiredTime) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expiredTime)
                .signWith(getSignInkey(), SignatureAlgorithm.HS256)
                .compact();
    }


    /*
    *   Token 검증
    */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        Claims claims = extractAllClaims(token);

        if (!claims.containsKey("role")) return false;
        if (!claims.containsKey("name")) return false;
        if (!claims.containsKey("profileImageUrl")) return false;

        String username = claims.getSubject();

        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    /*
    *   Token 정보 추출
    */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInkey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInkey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
