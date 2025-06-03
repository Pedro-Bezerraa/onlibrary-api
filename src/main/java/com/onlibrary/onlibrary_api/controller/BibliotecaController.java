package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.biblioteca.ContagemResponseDTO;
import com.onlibrary.onlibrary_api.dto.biblioteca.BibliotecaRequestDTO;
import com.onlibrary.onlibrary_api.dto.biblioteca.BibliotecaResponseDTO;
import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.exception.AuthenticationException;
import com.onlibrary.onlibrary_api.service.BibliotecaService;
import com.onlibrary.onlibrary_api.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
    public ResponseEntity<ResponseDTO<Map<String, UUID>>> criarBiblioteca(
            @RequestBody @Valid BibliotecaRequestDTO dto,
            HttpServletRequest request
    ) {
        String token = recuperarToken(request);

        if (token == null || token.isBlank()) {
            throw new AuthenticationException("Token ausente ou inválido");
        }

        UUID idUsuario = jwtService.extractIdForUser(token);
        UUID idBiblioteca = bibliotecaService.criarBiblioteca(dto, idUsuario);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(true, "Biblioteca criada com sucesso", Map.of("idBiblioteca", idBiblioteca)));
    }

    @GetMapping("/minhas-bibliotecas")
    public ResponseEntity<ResponseDTO<List<BibliotecaResponseDTO>>> listarMinhasBibliotecas(HttpServletRequest request) {
        String token = recuperarToken(request);

        if (token == null || token.isBlank()) {
            throw new AuthenticationException("Token ausente ou inválido");
        }

        List<BibliotecaResponseDTO> bibliotecas = bibliotecaService.listarBibliotecasAdminOuFuncionario(token);

        return ResponseEntity.ok(new ResponseDTO<>(true, "Bibliotecas recuperadas com sucesso", bibliotecas));
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
