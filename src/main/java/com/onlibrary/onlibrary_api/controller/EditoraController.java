package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.editora.UpdateEditoraRequestDTO;
import com.onlibrary.onlibrary_api.dto.editora.UpdateEditoraResponseDTO;
import com.onlibrary.onlibrary_api.dto.editora.EditoraRequestDTO;
import com.onlibrary.onlibrary_api.dto.editora.EditoraResponseDTO;
import com.onlibrary.onlibrary_api.service.EditoraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/editora")
@RequiredArgsConstructor
public class EditoraController {
    private final EditoraService editoraService;

    @PostMapping("/criar-editora")
    public ResponseEntity<?> criarEditora(@RequestBody EditoraRequestDTO dto) {
        EditoraResponseDTO editora = editoraService.criarEditora(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(true, "Editora criada com sucesso", editora));
    }


    @PutMapping("/atualizar-editora/{id}")
    public ResponseEntity<?> atualizarEditora(@PathVariable UUID id, @RequestBody UpdateEditoraRequestDTO dto) {
        UpdateEditoraResponseDTO editoraAtualizada = editoraService.atualizar(id, dto);
        return ResponseEntity.ok()
                .body(new ResponseDTO<>(true, "Editora criada com sucesso", editoraAtualizada));
    }
}
