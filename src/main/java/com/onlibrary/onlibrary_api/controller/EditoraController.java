package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.editora.*;
import com.onlibrary.onlibrary_api.service.EditoraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/editora")
@RequiredArgsConstructor
public class EditoraController {
    private final EditoraService editoraService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<EditoraResponseDTO>>> listarEditoras() {
        List<EditoraResponseDTO> editoras = editoraService.listarEditoras();
        return ResponseEntity.ok(new ResponseDTO<>(true, "Editoras recuperadas com sucesso", editoras));
    }

    @PostMapping("/criar-editora")
    public ResponseEntity<?> criarEditora(@RequestBody EditoraRequestDTO dto) {
        EditoraResponseDTO editora = editoraService.criarEditora(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(true, "Editora criada com sucesso", editora));
    }

    @GetMapping("/dependencies/{id}")
    public ResponseEntity<ResponseDTO<EditoraDependenciesDTO>> getEditoraDependencies(@PathVariable UUID id) {
        EditoraDependenciesDTO dependencies = editoraService.getEditoraDependencies(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Dependências da editora recuperadas com sucesso.", dependencies));
    }

    @PutMapping("/atualizar-editora/{id}")
    public ResponseEntity<?> atualizarEditora(@PathVariable UUID id, @RequestBody UpdateEditoraRequestDTO dto) {
        UpdateEditoraResponseDTO editoraAtualizada = editoraService.atualizar(id, dto);
        return ResponseEntity.ok()
                .body(new ResponseDTO<>(true, "Editora criada com sucesso", editoraAtualizada));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<ResponseDTO<Void>> deletarEditora(@PathVariable UUID id) {
        editoraService.deletarEditora(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Editora marcada como deletada com sucesso.", null));
    }
}
