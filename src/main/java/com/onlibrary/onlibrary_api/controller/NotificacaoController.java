package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notificacao")
@RequiredArgsConstructor
public class NotificacaoController {

    private final NotificacaoService notificacaoService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<?>>> getNotificacoes(
            @RequestParam("usuarioId") UUID usuarioId,
            @RequestParam(name = "bibliotecaId", required = false) UUID bibliotecaId,
            @RequestParam("tipo") String tipo) {

        List<?> notificacoes = notificacaoService.getNotificacoes(usuarioId, bibliotecaId, tipo);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Notificações recuperadas com sucesso.", notificacoes));
    }

    @PutMapping("/lida/{id}")
    public ResponseEntity<ResponseDTO<Void>> marcarComoLida(@PathVariable UUID id) {
        notificacaoService.marcarComoLida(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Notificação marcada como lida.", null));
    }

    @DeleteMapping("/deletar")
    public ResponseEntity<ResponseDTO<Void>> deletarItem(
            @RequestParam("type") String tipo,
            @RequestParam("id_biblioteca") UUID id) {
        notificacaoService.deletarNotificacaoOuContato(tipo, id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Item marcado como deletado com sucesso.", null));
    }
}