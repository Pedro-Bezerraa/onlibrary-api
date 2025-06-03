package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.usuarioBiblioteca.AttUsuarioBibliotecaRequestDTO;
import com.onlibrary.onlibrary_api.dto.usuarioBiblioteca.UsuarioBibliotecaRequestDTO;
import com.onlibrary.onlibrary_api.dto.usuarioBiblioteca.UsuarioBibliotecaResponseDTO;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.service.UsuarioBibliotecaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/usuarioBiblioteca")
@RequiredArgsConstructor
public class UsuarioBibliotecaController {
    private final UsuarioBibliotecaService usuarioBibliotecaService;

    @PostMapping("/criar-usuarioBiblioteca")
    public ResponseEntity<?> criarUsuarioBiblioteca(@RequestBody UsuarioBibliotecaRequestDTO dto) {
        UsuarioBibliotecaResponseDTO usuarioBiblioteca = usuarioBibliotecaService.criarUsuarioBiblioteca(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(true, "Relação usuário-biblioteca criada com sucesso!", usuarioBiblioteca));
    }

    @PutMapping("/atualizar-usuarioBiblioteca/{id}")
    public ResponseEntity<?> atualizarUsuarioBiblioteca(@PathVariable UUID id, @RequestBody AttUsuarioBibliotecaRequestDTO dto) {
        UsuarioBibliotecaResponseDTO usuarioBiblioteca = usuarioBibliotecaService.atualizarUsuarioBiblioteca(dto, id);
        return ResponseEntity.ok(
                new ResponseDTO<>(true, "Relação usuário-biblioteca atualizada com sucesso!", usuarioBiblioteca)
        );
    }
}
