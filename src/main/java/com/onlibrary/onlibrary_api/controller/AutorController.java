package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.autor.AutorRequestDTO;
import com.onlibrary.onlibrary_api.dto.autor.AutorResponseDTO;
import com.onlibrary.onlibrary_api.exception.InvalidCredentialsException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.service.AutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/autor")
@RequiredArgsConstructor
public class AutorController {
    private final AutorService autorService;

    @PostMapping("/criar-autor")
    public ResponseEntity<?> criarAutor(@RequestBody AutorRequestDTO dto) {
        AutorResponseDTO autor = autorService.criarAutor(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(true, "Autor criado com sucesso!", autor));
    }


    @PutMapping("/atualizar-autor/{id}")
    public ResponseEntity<?> atualizarAutor(@PathVariable UUID id, @RequestBody AutorRequestDTO dto) {
        AutorResponseDTO autorAtualizado = autorService.atualizarAutor(id, dto);
        return ResponseEntity.ok(
                new ResponseDTO<>(true, "Autor atualizado com sucesso!", autorAtualizado)
        );
    }
}
