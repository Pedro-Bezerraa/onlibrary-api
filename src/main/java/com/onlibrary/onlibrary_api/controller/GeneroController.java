package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.genero.GeneroDependenciesDTO;
import com.onlibrary.onlibrary_api.dto.genero.GeneroRequestDTO;
import com.onlibrary.onlibrary_api.dto.genero.GeneroResponseDTO;
import com.onlibrary.onlibrary_api.exception.InvalidCredentialsException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.service.GeneroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/genero")
@RequiredArgsConstructor
public class GeneroController {
    private final GeneroService generoService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<GeneroResponseDTO>>> listarGeneros() {
        List<GeneroResponseDTO> generos = generoService.listarGeneros();
        return ResponseEntity.ok(new ResponseDTO<>(true, "Gêneros recuperados com sucesso", generos));
    }

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

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<ResponseDTO<Void>> deletarGenero(@PathVariable UUID id) {
        generoService.deletarGenero(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Gênero marcado como deletado com sucesso.", null));
    }

    @GetMapping("/dependencies/{id}")
    public ResponseEntity<ResponseDTO<GeneroDependenciesDTO>> getGeneroDependencies(@PathVariable UUID id) {
        GeneroDependenciesDTO dependencies = generoService.getGeneroDependencies(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Dependências do gênero recuperadas com sucesso.", dependencies));
    }
}
