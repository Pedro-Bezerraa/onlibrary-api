package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.model.entities.Notificacao;
import com.onlibrary.onlibrary_api.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}