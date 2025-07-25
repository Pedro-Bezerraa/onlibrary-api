package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.autor.AutorDependenciesDTO;
import com.onlibrary.onlibrary_api.dto.autor.AutorRequestDTO;
import com.onlibrary.onlibrary_api.dto.autor.AutorResponseDTO;
import com.onlibrary.onlibrary_api.exception.InvalidCredentialsException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.service.AutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/autor")
@RequiredArgsConstructor
public class AutorController {
    private final AutorService autorService;

    @GetMapping("/dependencies/{id}")
    public ResponseEntity<ResponseDTO<AutorDependenciesDTO>> getAutorDependencies(@PathVariable UUID id) {
        AutorDependenciesDTO dependencies = autorService.getAutorDependencies(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Dependências do autor recuperadas com sucesso.", dependencies));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<AutorResponseDTO>>> listarAutores() {
        List<AutorResponseDTO> autores = autorService.listarAutores();
        return ResponseEntity.ok(new ResponseDTO<>(true, "Autores recuperados com sucesso", autores));
    }

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

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<ResponseDTO<Void>> deletarAutor(@PathVariable UUID id) {
        autorService.deletarAutor(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Autor marcado como deletado com sucesso.", null));
    }
}
