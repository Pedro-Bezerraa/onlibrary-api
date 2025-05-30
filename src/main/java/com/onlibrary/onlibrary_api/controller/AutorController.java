package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.AutorRequestDTO;
import com.onlibrary.onlibrary_api.exception.InvalidCredentialsException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.service.AutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/autor")
@RequiredArgsConstructor
public class AutorController {
    private final AutorService autorService;

    @PostMapping("/criar-autor")
    public ResponseEntity<?> criarAutor(@RequestBody AutorRequestDTO dto) {
        try {
            autorService.criarAutor(dto);
            return ResponseEntity.ok("Autor criado com sucesso");
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/atualizar-autor/{id}")
    public ResponseEntity<?> atualizarAutor(@PathVariable UUID id, @RequestBody AutorRequestDTO dto) {
        try {
            autorService.atualizarAutor(id, dto);
            return ResponseEntity.ok("Autor atualizado com sucesso com sucesso");
        } catch (InvalidCredentialsException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
