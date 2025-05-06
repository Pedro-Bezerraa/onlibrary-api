package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.biblioteca.BibliotecaDTO;
import com.onlibrary.onlibrary_api.dto.biblioteca.CreateBibliotecaDTO;
import com.onlibrary.onlibrary_api.service.BibliotecaService;
import com.onlibrary.onlibrary_api.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bibliotecas")
@RequiredArgsConstructor
public class BibliotecaController {
    private final BibliotecaService bibliotecaService;
    private final JwtService jwtService;

    @PostMapping("/criar-biblioteca")
    public ResponseEntity<?> criarBiblioteca(
            @RequestBody @Valid CreateBibliotecaDTO dto,
            @CookieValue(name = "jwt", required = false) String token
    ) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token ausente");
        }

        Long idUsuario = jwtService.extractUserId(token);

        Long idBiblioteca = bibliotecaService.criarBiblioteca(dto, idUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("idBiblioteca", idBiblioteca));
    }

    @GetMapping("/minhas")
    public ResponseEntity<?> listarMinhasBibliotecas(HttpServletRequest request) {
        String token = recuperarToken(request);
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token ausente");
        }

        Long usuarioId = jwtService.extractUserId(token);
        if (usuarioId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido");
        }

        List<BibliotecaDTO> bibliotecas = bibliotecaService.buscarBibliotecasDoUsuarioDTO(usuarioId);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Bibliotecas encontradas", bibliotecas));
    }

    private String recuperarToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("jwt".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
