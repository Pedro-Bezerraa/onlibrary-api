package com.onlibrary.onlibrary_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlibrary.onlibrary_api.dto.livro.*;
import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.model.views.VwBibliotecaReservaExemplar;
import com.onlibrary.onlibrary_api.model.views.VwLivro;
import com.onlibrary.onlibrary_api.model.views.VwTableBibliotecaLivro;
import com.onlibrary.onlibrary_api.service.LivroService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.onlibrary.onlibrary_api.repository.views.VwLivroRepository.SuggestionProjection;


import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/livro")
@AllArgsConstructor
@Slf4j
public class LivroController {
    private final LivroService livroService;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "/criar-livro", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> criarLivro(
            @RequestPart("data") String dataJson,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem
    ) throws Exception {
        log.info("--- Recebendo dados para criar livro ---");
        log.info("JSON recebido: {}", dataJson);

        if (imagem != null && !imagem.isEmpty()) {
            log.info("Arquivo de imagem recebido: Nome='{}', Tamanho='{} bytes', Tipo='{}'",
                    imagem.getOriginalFilename(), imagem.getSize(), imagem.getContentType());
        } else {
            log.warn("Nenhum arquivo de imagem foi enviado.");
        }
        log.info("------------------------------------");

        LivroRequestDTO dto = objectMapper.readValue(dataJson, LivroRequestDTO.class);
        LivroResponseDTO livroResponseDTO = livroService.criarLivro(dto, imagem);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(true, "Livro criado com sucesso.", livroResponseDTO));
    }

    @PutMapping(value = "/atualizar-livro/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> atualizarLivro(
            @PathVariable UUID id,
            @RequestPart(required = false) UpdateLivroRequestDTO dto,
            @RequestPart(required = false) MultipartFile imagem
    ) {
        log.info("--- Recebendo dados para atualizar livro com ID: {} ---", id);
        if (dto != null) {
            log.info("DTO recebido: {}", dto.toString());
        } else {
            log.info("Nenhum DTO de atualização foi enviado.");
        }

        if (imagem != null && !imagem.isEmpty()) {
            log.info("Arquivo de imagem recebido: Nome='{}', Tamanho='{} bytes', Tipo='{}'",
                    imagem.getOriginalFilename(), imagem.getSize(), imagem.getContentType());
        } else {
            log.warn("Nenhum arquivo de imagem foi enviado para atualização.");
        }
        log.info("------------------------------------");

        UpdateLivroResponseDTO response = livroService.atualizarLivro(id, dto, imagem);
        return ResponseEntity.ok()
                .body(new ResponseDTO<>(true, "Livro atualizado com sucesso.", response));
    }

    @GetMapping("/search/biblioteca")
    public ResponseEntity<ResponseDTO<List<VwTableBibliotecaLivro>>> searchInBiblioteca(
            @RequestParam("id_biblioteca") UUID bibliotecaId,
            @RequestParam(required = false) String value,
            @RequestParam(defaultValue = "todos") String filter) {
        List<VwTableBibliotecaLivro> result = livroService.searchLivrosInBiblioteca(value, filter, bibliotecaId);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Pesquisa de livros na biblioteca realizada com sucesso.", result));
    }

    @GetMapping("/search/home")
    public ResponseEntity<ResponseDTO<List<LivroHomePageSearchDTO>>> searchInHomePage(
            @RequestParam(required = false) String value,
            @RequestParam(defaultValue = "todos") String filter) {
        List<LivroHomePageSearchDTO> result = livroService.searchLivrosHomePage(value, filter);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Pesquisa de livros realizada com sucesso.", result));
    }

    @GetMapping("/search/suggestions")
    public ResponseEntity<ResponseDTO<List<SuggestionProjection>>> getSearchSuggestions(
            @RequestParam(required = false) String value) {
        List<SuggestionProjection> result = livroService.getSearchSuggestions(value);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Sugestões de pesquisa recuperadas com sucesso.", result));
    }

    @GetMapping("/{livroId}/libraries")
    public ResponseEntity<ResponseDTO<List<VwBibliotecaReservaExemplar>>> getBibliotecasForLivro(@PathVariable UUID livroId) {
        List<VwBibliotecaReservaExemplar> result = livroService.getBibliotecasForLivro(livroId);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Bibliotecas para o livro recuperadas com sucesso.", result));
    }

    @GetMapping("/{livroId}/details")
    public ResponseEntity<ResponseDTO<VwLivro>> getLivroDetails(@PathVariable UUID livroId) {
        VwLivro result = livroService.getLivroDetails(livroId);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Detalhes do livro recuperados com sucesso.", result));
    }

    @GetMapping("/dependencies/{id}")
    public ResponseEntity<ResponseDTO<Map<String, Object>>> getLivroDependenciesForUpdate(@PathVariable UUID id) {
        Map<String, Object> dependencies = livroService.getLivroDependenciesForUpdate(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Dependências do livro para atualização recuperadas com sucesso.", dependencies));
    }

    @GetMapping("/book-page/{id}")
    public ResponseEntity<ResponseDTO<BookPageDTO>> getBookPageInfo(@PathVariable UUID id) {
        BookPageDTO bookInfo = livroService.getBookPageInfo(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Informações do livro para a página recuperadas com sucesso.", bookInfo));
    }
}
