package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.categoria.AttCategoriaRequestDTO;
import com.onlibrary.onlibrary_api.dto.categoria.AttCategoriaResponseDTO;
import com.onlibrary.onlibrary_api.dto.categoria.CategoriaRequestDTO;
import com.onlibrary.onlibrary_api.dto.categoria.CategoriaResponseDTO;
import com.onlibrary.onlibrary_api.exception.InvalidCredentialsException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/categoria")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService categoriaService;

    @PostMapping("/criar-categoria")
    public ResponseEntity<?> criarCategoria(@RequestBody CategoriaRequestDTO dto) {
        CategoriaResponseDTO categoria = categoriaService.criarCategoria(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(true, "Categoria criada com sucesso", categoria));
    }


    @PutMapping("/atualizar-categoria/{id}")
    public ResponseEntity<?> atualizarCategoria(@PathVariable UUID id, @RequestBody AttCategoriaRequestDTO dto) {
        AttCategoriaResponseDTO categoriaAtualizado =categoriaService.atualizarCategoria(id, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(true, "Categoria atualizada com sucesso", categoriaAtualizado));
    }
}
