package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.biblioteca.AutorDTO;
import com.onlibrary.onlibrary_api.service.LivroService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/livros")
@AllArgsConstructor
public class LivroController {
    private LivroService livroService;

    @GetMapping("/autores")
    public ResponseEntity<List<AutorDTO>> listarAutores() {
        List<AutorDTO> autores = livroService.listarAutores();
        return ResponseEntity.ok(autores);
    }
}
