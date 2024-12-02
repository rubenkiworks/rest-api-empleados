package com.example.springjwt.security.jwt;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.springjwt.security.services.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${demo.app.jwtSecret}")
    private String jwtSecret;

    @Value("${demo.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authenticaion) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authenticaion.getPrincipal();

        return Jwts.builder()
            .subject(userPrincipal.getUsername())
            .issuedAt(new Date())
            .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
            .signWith(key())
            .compact();

            // .setSubject(userPrincipal.getUsername())
            // .setIssuedAt(new Date())
            // .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
            // .signWith(key(), SignatureAlgorithm.HS256)
            // .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {

        return Jwts.parser().verifyWith((SecretKey) key()).build()
            .parseSignedClaims(token).getPayload().getSubject();

        // return Jwts.parserBuilder().setSigningKey(key()).build()
        //     .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {

        try {
            Jwts.parser().verifyWith((SecretKey) key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("JWT token is exprired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("JWT Token is unsupported; {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("JWT claims string is empty: {}", e.getMessage());
        }


        return false;
    }
}
