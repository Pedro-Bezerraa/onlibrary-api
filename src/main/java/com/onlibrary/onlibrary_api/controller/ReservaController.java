package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.reserva.AttReservaRequestDTO;
import com.onlibrary.onlibrary_api.dto.reserva.AttReservaResponseDTO;
import com.onlibrary.onlibrary_api.dto.reserva.ReservaRequestDTO;
import com.onlibrary.onlibrary_api.dto.reserva.ReservaResponseDTO;
import com.onlibrary.onlibrary_api.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/reserva")
@RequiredArgsConstructor
public class ReservaController {
    private final ReservaService reservaService;

    @PostMapping("/criar-reserva")
    public ResponseEntity<?> reservar(@RequestBody ReservaRequestDTO dto) {
        ReservaResponseDTO reserva = reservaService.criarReserva(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(true, "Reserva efetuada com sucesso.", reserva));
    }

    @PutMapping("/atualiza-reserva/{reservaId}")
    public ResponseEntity<?> reservar(@PathVariable UUID reservaId, @RequestBody AttReservaRequestDTO dto) {
        AttReservaResponseDTO reserva = reservaService.atualizarReserva(reservaId, dto);
        return ResponseEntity.ok()
                .body(new ResponseDTO<>(true, "Reserva atualizada com sucesso.", reserva));
    }
}
