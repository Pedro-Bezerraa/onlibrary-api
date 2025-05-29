package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.AttPerfilUsuarioRequestDTO;
import com.onlibrary.onlibrary_api.dto.PerfilUsuarioRequestDTO;
import com.onlibrary.onlibrary_api.exception.InvalidCredentialsException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/perfil")
@RequiredArgsConstructor
public class PerfilController {
    private final PerfilService perfilService;

    @PostMapping("/criar-perfil")
    public ResponseEntity<?> criarPerfil(@RequestBody PerfilUsuarioRequestDTO dto) {
        try {
            perfilService.criarPerfil(dto);
            return ResponseEntity.ok("Perfil criado com sucesso");
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/atualizar-perfil/{id}")
    public ResponseEntity<?> atualizarPerfil(@RequestBody AttPerfilUsuarioRequestDTO dto, @PathVariable UUID id) {
        try {
            perfilService.atualizarPerfil(dto, id);
            return ResponseEntity.ok("Perfil atualizado com sucesso");
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
