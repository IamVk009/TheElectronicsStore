package com.lucifer.electronics.store.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtHelper {

//  Token validity duration
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

//  This will be encoded when JWT will get created i.e. this secret will be used to generate JWT token.
    @Value("${jwt.secret}")
    private String secret;

//  Retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

//  Retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * This method retrieves a specific claim from a JWT (JSON Web Token) by applying a function to the claims contained within the token
     *
     * @param token:         A string representing the JWT token from which to extract the claim.
     * @param claimsResolver - A Function that takes Claims as input and returns a value of type T.
     *                       This function is responsible for extracting the specific claim from the JWT.
     * @return: Result of applying the claimsResolver function to the claims object.
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

//  To retrieve any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

//  Check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

//  Generate new token for user
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

//  While creating the token -
//  1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
//  2. Sign the JWT using the HS512 algorithm and secret key.
//  3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
//  Compaction of the JWT to a URL-safe string

    /**
     * This method encapsulates the logic for generating a JWT with the specified claims and subject,
     * including issuance and expiration times, and signing the token with a secret key using the HS512 algorithm
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * This method provides a simple validation mechanism for JWTs by comparing the username extracted from the
     * token with the username from the UserDetails object and checking if the token has expired. If both checks pass,
     * the token is considered valid.
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

