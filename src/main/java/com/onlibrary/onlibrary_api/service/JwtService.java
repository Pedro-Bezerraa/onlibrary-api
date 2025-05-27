package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.model.entities.Usuario;
import com.onlibrary.onlibrary_api.repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {
    private final UsuarioRepository usuarioRepository;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    public String generateAcessToken(Authentication authentication) {
        return generateToken(authentication, jwtExpiration, new HashMap<>());
    }

    private String generateToken(Authentication authentication, long expiration, Map<String, String> customClaims) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        Usuario usuario = usuarioRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        Map<String, Object> claims = new HashMap<>(customClaims);
        claims.put("id", usuario.getId().toString());

        log.info("Token gerado para usuário com ID: {}", usuario.getId());
        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(usuario.getUsername())
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSignInKey())
                .compact();
    }

    public UUID extractIdForUser(String token) {
        Claims claims = extractAllClaims(token);
        Object idClaim = claims.get("id");
        if (idClaim instanceof String) {
            try {
                return UUID.fromString((String) idClaim);
            } catch (IllegalArgumentException e) {
                log.error("ID no token não é um UUID válido: {}", idClaim);
                throw new RuntimeException("ID no token inválido", e);
            }
        } else {
            log.error("ID no token não é uma string: {}", idClaim);
            return null;
        }
    }

    public boolean validateTokenForUser(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username != null && username.equals(userDetails.getUsername());
    }

    public String extractUsername(String token) {
        Claims claims = extractAllClaims(token);
        return claims != null ? claims.getSubject() : null;
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Token inválido", e);
        }
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}