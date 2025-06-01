package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.usuarioBiblioteca.AttUsuarioBibliotecaRequestDTO;
import com.onlibrary.onlibrary_api.dto.usuarioBiblioteca.UsuarioBibliotecaRequestDTO;
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
        try {
            usuarioBibliotecaService.criarUsuarioBiblioteca(dto);
            return ResponseEntity.ok("relação usuario a biblioteca criado");
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/atualizar-usuarioBiblioteca/{id}")
    public ResponseEntity<?> atualizarUsuarioBiblioteca(@PathVariable UUID id, @RequestBody AttUsuarioBibliotecaRequestDTO dto) {
        try {
            usuarioBibliotecaService.atualizarUsuarioBiblioteca(dto, id);
            return ResponseEntity.ok("relação usuario a biblioteca atualizado");
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
