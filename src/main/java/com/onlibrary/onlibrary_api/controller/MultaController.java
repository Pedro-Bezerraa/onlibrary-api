package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.multa.AttMultaRequestDTO;
import com.onlibrary.onlibrary_api.dto.multa.AttMultaResponseDTO;
import com.onlibrary.onlibrary_api.dto.multa.MultaRequestDTO;
import com.onlibrary.onlibrary_api.dto.multa.MultaResponseDTO;
import com.onlibrary.onlibrary_api.service.MultaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/multa")
@RequiredArgsConstructor
public class MultaController {
    private final MultaService multaService;


    @PostMapping("/criar-multa")
    public ResponseEntity<?> criarMulta(
            @RequestBody MultaRequestDTO dto
    ) {
        MultaResponseDTO multa = multaService.cadastrarMulta(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(true, "Multa realizada com sucesso.", multa));
    }

    @PutMapping("/atualizar-multa/{multaId}")
    public ResponseEntity<?> atualizarMulta(
            @PathVariable UUID multaId,
            @RequestBody AttMultaRequestDTO dto
    ) {
        AttMultaResponseDTO multa = multaService.atualizarMulta(multaId, dto);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Multa atualizada com sucesso.", multa));
    }
}
