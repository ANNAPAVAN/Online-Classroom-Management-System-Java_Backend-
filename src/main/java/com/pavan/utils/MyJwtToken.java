package com.pavan.utils;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.pavan.dto.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class MyJwtToken {

    private static String secret = "This_is_secret";
    private static long expiryDuration = 60 * 60;     // 1 hour

    public String generateJwt(Users user){

        long milliTime = System.currentTimeMillis();
        long expiryTime = milliTime + expiryDuration * 1000;

        Date issuedAt = new Date(milliTime);
        Date expiryAt = new Date(expiryTime);

        // claims
        Claims claims = Jwts.claims()
                .setIssuer(user.getEmail().toString())
                .setIssuedAt(issuedAt)
                .setExpiration(expiryAt);
 
        // optional claims
        claims.put("type", user.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    
    public Claims verify(String authorization) throws Exception {

        try {
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authorization).getBody();
            // Printing token details
            System.out.println("Token issuer: " + claims.getIssuer());
            System.out.println("Token issued at: " + claims.getIssuedAt());
            System.out.println("Token expiration: " + claims.getExpiration());
            System.out.println("Token type: " + claims.get("type"));
            return claims;
        } catch(Exception e) {
            return null;
        }

    }
    
}
