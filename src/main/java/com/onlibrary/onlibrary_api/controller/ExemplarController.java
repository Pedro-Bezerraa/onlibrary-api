package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.exemplar.*;
import com.onlibrary.onlibrary_api.model.enums.SituacaoExemplar;
import com.onlibrary.onlibrary_api.model.views.VwTableExemplar;
import com.onlibrary.onlibrary_api.service.ExemplarService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/exemplar")
@RequiredArgsConstructor
public class ExemplarController {
    private final ExemplarService exemplarService;

    @PostMapping("/criar-exemplar")
    public ResponseEntity<?> criarExemplar(@RequestBody ExemplarRequestDTO dto) {
        ExemplarResponseDTO exemplar = exemplarService.criarExemplar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(true, "Exemplar criado com sucesso!", exemplar));
    }

    @PutMapping("/atualizar-exemplar/{id}")
    public ResponseEntity<?> atualizarExemplar(@PathVariable UUID id, @RequestBody UpdateExemplarRequestDTO dto) {
        ExemplarResponseDTO exemplarAtualizado = exemplarService.atualizarExemplar(id, dto);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Exemplar atualizado com sucesso!", exemplarAtualizado));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<ResponseDTO<Void>> deletarExemplar(@PathVariable UUID id) {
        exemplarService.deletarExemplar(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Exemplar marcado como deletado com sucesso.", null));
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO<List<VwTableExemplar>>> searchExemplares(
            @RequestParam("id_biblioteca") UUID bibliotecaId,
            @RequestParam(required = false) String value,
            @RequestParam(defaultValue = "todos") String filter) {
        List<VwTableExemplar> result = exemplarService.searchExemplares(value, filter, bibliotecaId);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Pesquisa de exemplares realizada com sucesso.", result));
    }

    @GetMapping("/dependencies/{id}")
    public ResponseEntity<ResponseDTO<ExemplarDependenciesDTO>> getExemplarDependencies(
            @PathVariable UUID id,
            @RequestParam("id_biblioteca") UUID bibliotecaId) {
        ExemplarDependenciesDTO dependencies = exemplarService.getExemplarDependencies(id, bibliotecaId);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Dependências do exemplar recuperadas com sucesso.", dependencies));
    }

    @GetMapping("/situacoes")
    public ResponseEntity<ResponseDTO<List<ExemplarStatusDTO>>> getSituacoes(
            @RequestParam("id_biblioteca") UUID bibliotecaId,
            @RequestParam("id_livro") UUID livroId) {
        List<ExemplarStatusDTO> situacoes = exemplarService.getExemplaresStatus(bibliotecaId, livroId);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Situações dos exemplares recuperadas com sucesso.", situacoes));
    }
}
