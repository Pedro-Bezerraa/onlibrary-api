package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.genero.GeneroRequestDTO;
import com.onlibrary.onlibrary_api.dto.genero.GeneroResponseDTO;
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
        GeneroResponseDTO genero = generoService.criarGenero(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(true, "Gênero criado com sucesso!", genero));
    }


    @PutMapping("/atualizar-genero/{id}")
    public ResponseEntity<?> atualizarGenero(@PathVariable UUID id, @RequestBody GeneroRequestDTO dto) {
        GeneroResponseDTO generoAtualizado = generoService.atualizarGenero(id, dto);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Gênero atualizado com sucesso!", generoAtualizado));
    }

}
