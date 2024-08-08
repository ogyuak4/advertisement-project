package org.example.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.*;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class JwtProvider
{
    @Value("${authentication.jwt.expiration-in-ms}")
    private Long JWT_EXPIRATION_IN_MS;  // Token expiration time in milliseconds

    private static final String JWT_TOKEN_PREFIX = "Bearer"; // Prefix for JWT in headers
    private static final String JWT_HEADER_STRING = "Authorization"; // Header name for JWT


    private final PrivateKey jwtPrivateKey;
    private final PublicKey jwtPublicKey;


    public JwtProvider(@Value("${authentication.jwt.private-key}") String jwtPrivateKeyStr,
                       @Value("${authentication.jwt.public-key}") String jwtPublicKeyStr)
    {
        KeyFactory keyFactory = getKeyFactory();

        try {
            Base64.Decoder decoder = Base64.getDecoder(); // Create a Base64 decoder
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(decoder.decode(jwtPrivateKeyStr.getBytes())); // Decode and create private key spec
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(decoder.decode(jwtPublicKeyStr.getBytes())); // Decode and create public key spec

            jwtPrivateKey = keyFactory.generatePrivate(privateKeySpec); // Generate private key
            jwtPublicKey = keyFactory.generatePublic(publicKeySpec); // Generate public key
        } catch (Exception e) {
            throw new RuntimeException("Invalid key specification", e);
        }
    }


    public String generateToken(UserPrincipal authentication) // Generate a JWT token based on the provided UserPrincipal
    {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());

        return Jwts.builder().setSubject(authentication.getUsername()) // Build and return the JWT token
                .claim("userId", authentication.getId())
                .claim("roles", authorities)
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_IN_MS))
                .signWith(jwtPrivateKey, SignatureAlgorithm.RS512)
                .compact();
    }



    public Authentication getAuthentication(HttpServletRequest request)
    {
        String token = resolveToken(request);
        if (token == null)
        {
            return null;
        }
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtPublicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();
        Long userId = claims.get("userId", Long.class);
        List<GrantedAuthority> authorities = Arrays.stream(claims.get("roles").toString().split(","))
                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UserDetails userDetails = new UserPrincipal(userId, username, null);
        return username != null ? new UsernamePasswordAuthenticationToken(userDetails, null, authorities) : null;
    }





    public boolean isTokenValid(HttpServletRequest request)
    {
        String token = resolveToken(request);
        if (token == null)
        {
            return false;
        }
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtPublicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        if (claims.getExpiration().before(new Date()))
        {
            return false;
        }
        return true;
    }

    private String resolveToken(HttpServletRequest request) // Extract the JWT token from the Authorization header
    {
        String bearerToken = request.getHeader(JWT_HEADER_STRING);
        if (bearerToken != null && bearerToken.startsWith(JWT_TOKEN_PREFIX))
        {
            return bearerToken.substring(7); // Remove the "Bearer" prefix and return the token
        }
        return null;
    }

    private KeyFactory getKeyFactory() // Get KeyFactory instance for RSA algorithm
    {
        try
        {
            return KeyFactory.getInstance("RSA");
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException("Unknown key generation algorithm", e);
        }
    }
}