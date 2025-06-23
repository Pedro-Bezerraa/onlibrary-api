package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.usuarioBiblioteca.UpdateUsuarioBibliotecaRequestDTO;
import com.onlibrary.onlibrary_api.dto.usuarioBiblioteca.UsuarioBibliotecaDependenciesDTO;
import com.onlibrary.onlibrary_api.dto.usuarioBiblioteca.UsuarioBibliotecaRequestDTO;
import com.onlibrary.onlibrary_api.dto.usuarioBiblioteca.UsuarioBibliotecaResponseDTO;
import com.onlibrary.onlibrary_api.model.views.VwTabelaUsuarioBiblioteca;
import com.onlibrary.onlibrary_api.service.UsuarioBibliotecaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarioBiblioteca")
@RequiredArgsConstructor
public class UsuarioBibliotecaController {
    private final UsuarioBibliotecaService usuarioBibliotecaService;

    @PostMapping("/criar-usuarioBiblioteca")
    public ResponseEntity<?> criarUsuarioBiblioteca(@RequestBody UsuarioBibliotecaRequestDTO dto) {
        UsuarioBibliotecaResponseDTO usuarioBiblioteca = usuarioBibliotecaService.criarUsuarioBiblioteca(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(true, "Relação usuário-biblioteca criada com sucesso!", usuarioBiblioteca));
    }

    @PutMapping("/atualizar-usuarioBiblioteca/{id}")
    public ResponseEntity<?> atualizarUsuarioBiblioteca(@PathVariable UUID id, @RequestBody UpdateUsuarioBibliotecaRequestDTO dto) {
        UsuarioBibliotecaResponseDTO usuarioBiblioteca = usuarioBibliotecaService.atualizarUsuarioBiblioteca(dto, id);
        return ResponseEntity.ok(
                new ResponseDTO<>(true, "Relação usuário-biblioteca atualizada com sucesso!", usuarioBiblioteca)
        );
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<ResponseDTO<Void>> deletarUsuarioBiblioteca(@PathVariable UUID id) {
        usuarioBibliotecaService.deletarUsuarioBiblioteca(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Relação Usuário-Biblioteca marcada como deletada com sucesso.", null));
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO<List<VwTabelaUsuarioBiblioteca>>> search(
            @RequestParam("id_biblioteca") UUID bibliotecaId,
            @RequestParam(required = false) String value,
            @RequestParam(defaultValue = "todos") String filter) {
        List<VwTabelaUsuarioBiblioteca> result = usuarioBibliotecaService.searchUsuariosInBiblioteca(value, filter, bibliotecaId);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Pesquisa de usuários da biblioteca realizada com sucesso.", result));
    }

    @GetMapping("/dependencies/{id}")
    public ResponseEntity<ResponseDTO<UsuarioBibliotecaDependenciesDTO>> getDependencies(@PathVariable UUID id) {
        UsuarioBibliotecaDependenciesDTO dependencies = usuarioBibliotecaService.getUsuarioBibliotecaDependencies(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Dependências da relação usuário-biblioteca recuperadas com sucesso.", dependencies));
    }
}
