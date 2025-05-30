package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.GeneroRequestDTO;
import com.onlibrary.onlibrary_api.exception.InvalidCredentialsException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.service.GeneroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/genero")
@RequiredArgsConstructor
public class GeneroController {
    private final GeneroService generoService;

    @PostMapping("/criar-genero")
    public ResponseEntity<?> criarGenero(@RequestBody GeneroRequestDTO dto) {
        try {
            generoService.criarGenero(dto);
            return ResponseEntity.ok("genero criado com sucesso");
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/atualizar-genero/{id}")
    public ResponseEntity<?> atualizarGenero(@PathVariable UUID id, @RequestBody GeneroRequestDTO dto) {
        try {
            generoService.atualizarGenero(id, dto);
            return ResponseEntity.ok("genero atualizado com sucesso");
        } catch (InvalidCredentialsException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
