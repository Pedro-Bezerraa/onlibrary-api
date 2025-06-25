package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.contato.ContatoRequestDTO;
import com.onlibrary.onlibrary_api.dto.contato.ContatoResponseDTO;
import com.onlibrary.onlibrary_api.service.ContatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/suporte")
@RequiredArgsConstructor
public class ContatoController {

    private final ContatoService contatoService;

    @PostMapping
    public ResponseEntity<ResponseDTO<ContatoResponseDTO>> criarContato(@RequestBody ContatoRequestDTO dto) {
        ContatoResponseDTO response = contatoService.criarContato(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(true, "Mensagem de suporte enviada com sucesso.", response));
    }

    @PutMapping("/lido/{id}")
    public ResponseEntity<ResponseDTO<Void>> marcarComoLido(@PathVariable UUID id) {
        contatoService.marcarComoLido(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Registro de suporte marcado como lido.", null));
    }

    @PutMapping("/concluido/{id}")
    public ResponseEntity<ResponseDTO<Void>> marcarComoConcluido(@PathVariable UUID id) {
        contatoService.marcarComoConcluido(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Registro de suporte marcado como concluído.", null));
    }
}