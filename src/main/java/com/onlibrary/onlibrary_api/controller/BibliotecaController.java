package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.biblioteca.*;
import com.onlibrary.onlibrary_api.service.BibliotecaService;
import com.onlibrary.onlibrary_api.service.JwtService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/biblioteca")
@RequiredArgsConstructor
@Slf4j
public class BibliotecaController {
    private final BibliotecaService bibliotecaService;
    private final JwtService jwtService;

    @PostMapping("/criar-biblioteca")
    public ResponseEntity<?> criarBiblioteca(
            @RequestBody @Valid CreateBibliotecaDTO dto,
            HttpServletRequest request
    ) {
        String token = recuperarToken(request);

        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token ausente");
        }

        UUID idUsuario = jwtService.extractIdForUser(token);

        UUID idBiblioteca = bibliotecaService.criarBiblioteca(dto, idUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("idBiblioteca", idBiblioteca));
    }

    @GetMapping("/minhas-bibliotecas")
    public ResponseEntity<?> listarMinhasBibliotecas(HttpServletRequest request) {
        String token = recuperarToken(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token ausente");
        }

        UUID usuarioId = jwtService.extractIdForUser(token);
        if (usuarioId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }

        List<CreateBibliotecaResponseDTO> bibliotecas = bibliotecaService.listarBibliotecasAdminOuFuncionario(token);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Bibliotecas encontradas", bibliotecas));
    }

    @GetMapping("/contagem/{type}/{id}")
    public ResponseEntity<ContagemResponseDTO> contarPorBibliotecas(
            @PathVariable String type,
            @PathVariable UUID id) {
        ContagemResponseDTO response = bibliotecaService.contarPorBiblioteca(type, id);
        return ResponseEntity.ok(response);
    }


    private String recuperarToken(HttpServletRequest request) {
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
}
