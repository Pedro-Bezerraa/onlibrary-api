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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/bibliotecas")
@AllArgsConstructor
@Slf4j
public class BibliotecaController {
    private BibliotecaService bibliotecaService;
    private JwtService jwtService;

    @PutMapping("/atualizar-emprestimo/{id}")
    public ResponseEntity<?> atualizarEmprestimo(
            @PathVariable UUID id,
            @RequestBody EmprestimoRequestDTO dto) {

        bibliotecaService.atualizarEmprestimo(id, dto);
        return ResponseEntity.ok("Empréstimo atualizado com sucesso.");
    }

    @PutMapping("/atualizar-exemplar/{id}")
    public ResponseEntity<?> atualizarExemplar(
            @PathVariable UUID id,
            @RequestBody ExemplarRequestDTO dto) {

        try {
            bibliotecaService.atualizarExemlar(id, dto);
            return ResponseEntity.ok("Exemplar atualizado com sucesso.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            log.error("Erro ao atualizar exemplar", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar exemplar.");
        }
    }

    @PostMapping("/criar-biblioteca")
    public ResponseEntity<?> criarBiblioteca(
            @RequestBody @Valid CreateBibliotecaDTO dto,
            @CookieValue(name = "jwt", required = false) String token
    ) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token ausente");
        }

        UUID idUsuario = jwtService.extractIdForUser(token);

        UUID idBiblioteca = bibliotecaService.criarBiblioteca(dto, idUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("idBiblioteca", idBiblioteca));
    }

    @GetMapping("/minhas")
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

    private String recuperarToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("jwt".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @GetMapping("/contagem/{type}/{id}")
    public ResponseEntity<ContagemResponseDTO> contarPorBibliotecas(
            @PathVariable String type,
            @PathVariable UUID id) {
        ContagemResponseDTO response = bibliotecaService.contarPorBiblioteca(type, id);
        return ResponseEntity.ok(response);
    }
}
