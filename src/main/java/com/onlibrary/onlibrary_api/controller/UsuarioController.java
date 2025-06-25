package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.usuario.UpdateUsuarioRequestDTO;
import com.onlibrary.onlibrary_api.dto.usuario.UpdateUsuarioResponseDTO;
import com.onlibrary.onlibrary_api.service.AuthService;
import com.onlibrary.onlibrary_api.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final AuthService authService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<Map<String, String>>> getUsuarioTipo(@PathVariable UUID id) {
        String tipoUsuario = usuarioService.getUsuarioTipo(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Tipo de usuário recuperado com sucesso.", Map.of("tipo", tipoUsuario)));
    }

    @PutMapping("/atualizar/{usuarioId}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable UUID usuarioId, @RequestBody UpdateUsuarioRequestDTO dto) {
        UpdateUsuarioResponseDTO usuarioAtualizado = authService.atualizarUsuario(usuarioId, dto);
        return ResponseEntity.ok()
                .body(new ResponseDTO<>(true, "Usuario atualizado com sucesso.", usuarioAtualizado));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable UUID id) {
        authService.deletarUsuario(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Usuário deletado com sucesso.", null));
    }
}
