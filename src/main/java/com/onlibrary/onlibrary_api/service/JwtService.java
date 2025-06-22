package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.model.entities.Usuario;
import com.onlibrary.onlibrary_api.repository.entities.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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

import static io.jsonwebtoken.Jwts.builder;

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

    public String extractTokenFromRequest(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
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
        return builder()
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
        try {
            return extractAllClaims(token).getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())  // ✔️ Verifica a assinatura
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}