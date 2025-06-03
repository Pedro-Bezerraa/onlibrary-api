package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.categoria.CategoriaRequestDTO;
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
    public ResponseEntity<ResponseDTO<Void>> criarCategoria(@RequestBody CategoriaRequestDTO dto) {
        categoriaService.criarCategoria(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(true, "Categoria criada com sucesso", null));
    }


    @PutMapping("/atualizar-categoria/{id}")
    public ResponseEntity<ResponseDTO<Void>> atualizarCategoria(@PathVariable UUID id, @RequestBody CategoriaRequestDTO dto) {
        categoriaService.atualizarCategoria(id, dto);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Categoria atualizada com sucesso", null));
    }

}
