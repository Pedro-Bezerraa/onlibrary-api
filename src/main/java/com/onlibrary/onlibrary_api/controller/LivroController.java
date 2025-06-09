package com.onlibrary.onlibrary_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.livro.AttLivroRequestDTO;
import com.onlibrary.onlibrary_api.dto.livro.AttLivroResponseDTO;
import com.onlibrary.onlibrary_api.dto.livro.LivroRequestDTO;
import com.onlibrary.onlibrary_api.dto.livro.LivroResponseDTO;
import com.onlibrary.onlibrary_api.service.LivroService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/livro")
@AllArgsConstructor
public class LivroController {
    private final LivroService livroService;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "/criar-livro", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> criarLivro(
            @RequestPart("data") String dataJson,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem
    ) throws Exception {
        LivroRequestDTO dto = objectMapper.readValue(dataJson, LivroRequestDTO.class);
        LivroResponseDTO livroResponseDTO = livroService.criarLivro(dto, imagem);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(true, "Livro criado com sucesso.", livroResponseDTO));
    }

    @PutMapping(value = "/atualizar-livro/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> atualizarLivro(
            @PathVariable UUID id,
            @RequestPart(required = false) AttLivroRequestDTO dto,
            @RequestPart(required = false) MultipartFile imagem
    ) {
        AttLivroResponseDTO response = livroService.atualizarLivro(id, dto, imagem);
        return ResponseEntity.ok()
                .body(new ResponseDTO<>(true, "Livro atualizado com sucesso.", response));
    }
}
