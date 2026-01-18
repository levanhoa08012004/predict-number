package com.example.predict_numbers.service.impl;

import com.example.predict_numbers.configuration.UserPrincipal;
import com.example.predict_numbers.service.JwtService;
import com.example.predict_numbers.util.enums.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.expiryHour}")
    private long expiryHour;

    @Value("${jwt.expiryDay}")
    private long expiryDay;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.refreshKey}")
    private String refreshKey;

    @Override
    public String generateAccessToken(UserDetails userDetails) {
        return generateAccessToken(new HashMap<>(), userDetails);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return generateRefreshToken(new HashMap<>(), userDetails);
    }

    @Override
    public String extractUsername(String token, TokenType type) {
        return extractClaim(token, type, Claims::getSubject);
    }



    @Override
    public boolean isValid(String token, TokenType type, UserDetails userDetails) {
        final String username = extractUsername(token, type);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token, type);
    }


    private String generateAccessToken(Map<String, Object> claims,UserDetails userDetails) {
        if (userDetails instanceof UserPrincipal principal) {
            claims.put("userId", principal.getId());
        }
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * expiryHour))
                .signWith(getKey(TokenType.ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }
    private String generateRefreshToken(Map<String, Object> claims,UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * expiryHour))
                .signWith(getKey(TokenType.REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey(TokenType type) {
        byte[] keyBytes;
        if(TokenType.ACCESS_TOKEN.equals(type)){
            keyBytes = Decoders.BASE64.decode(secretKey);
        }else{
            keyBytes = Decoders.BASE64.decode(refreshKey);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean isTokenExpired(String token, TokenType type) {
        return extractExpiration(token, type).before(new Date());
    }
    private <T> T extractClaim(String token, TokenType type, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(token, type);
        return claimResolver.apply(claims);
    }
    private Claims extractAllClaims(String token, TokenType type) {
        return Jwts.parserBuilder().setSigningKey(getKey(type)).build().parseClaimsJws(token).getBody();
    }
    @Override
    public Date extractExpiration(String token, TokenType type) {
        return extractAllClaims(token, type).getExpiration();
    }
}
