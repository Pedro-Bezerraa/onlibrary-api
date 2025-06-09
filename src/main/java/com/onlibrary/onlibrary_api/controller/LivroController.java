package com.onlibrary.onlibrary_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.livro.LivroRequestDTO;
import com.onlibrary.onlibrary_api.dto.livro.LivroResponseDTO;
import com.onlibrary.onlibrary_api.service.LivroService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/livro")
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

//
//    @GetMapping("/autores")
//    public ResponseEntity<List<AutorDTO>> listarAutores() {
//        List<AutorDTO> autores = livroService.listarAutores();
//        return ResponseEntity.ok(autores);
//    }
}
