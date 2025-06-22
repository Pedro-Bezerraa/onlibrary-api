package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.perfilUsuario.UpdatePerfilUsuarioRequestDTO;
import com.onlibrary.onlibrary_api.dto.perfilUsuario.PerfilUsuarioRequestDTO;
import com.onlibrary.onlibrary_api.dto.perfilUsuario.PerfilUsuarioResponseDTO;
import com.onlibrary.onlibrary_api.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/perfil")
@RequiredArgsConstructor
public class PerfilController {
    private final PerfilService perfilService;

    @PostMapping("/criar-perfil")
    public ResponseEntity<?> criarPerfil(@RequestBody PerfilUsuarioRequestDTO dto) {
        PerfilUsuarioResponseDTO perfil = perfilService.criarPerfil(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(true, "Perfil criado com sucesso!", perfil));
    }


    @PutMapping("/atualizar-perfil/{id}")
    public ResponseEntity<?> atualizarPerfil(@RequestBody UpdatePerfilUsuarioRequestDTO dto, @PathVariable UUID id) {
        PerfilUsuarioResponseDTO perfilAtualizado = perfilService.atualizarPerfil(dto, id);
        return ResponseEntity.ok(
                new ResponseDTO<>(true, "Perfil atualizado com sucesso!", perfilAtualizado)
        );
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<ResponseDTO<Void>> deletarPerfilUsuario(@PathVariable UUID id) {
        perfilService.deletarPerfilUsuario(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Perfil de usuário marcado como deletado com sucesso.", null));
    }
}
