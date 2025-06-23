package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.reserva.*;
import com.onlibrary.onlibrary_api.model.views.VwTableReserva;
import com.onlibrary.onlibrary_api.service.ReservaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<?> reservar(@PathVariable UUID reservaId, @RequestBody UpdateReservaRequestDTO dto) {
        UpdateReservaResponseDTO reserva = reservaService.atualizarReserva(reservaId, dto);
        return ResponseEntity.ok()
                .body(new ResponseDTO<>(true, "Reserva atualizada com sucesso.", reserva));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<ResponseDTO<Void>> deletarReserva(@PathVariable UUID id) {
        reservaService.deletarReserva(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Reserva marcada como deletada com sucesso.", null));
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO<List<VwTableReserva>>> search(
            @RequestParam("id_biblioteca") UUID bibliotecaId,
            @RequestParam(required = false) String value,
            @RequestParam(defaultValue = "todos") String filter) {
        List<VwTableReserva> result = reservaService.searchReservas(value, filter, bibliotecaId);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Pesquisa de reservas realizada com sucesso.", result));
    }

    @GetMapping("/dependencies/{id}")
    public ResponseEntity<ResponseDTO<ReservaDependenciesDTO>> getDependencies(@PathVariable UUID id) {
        ReservaDependenciesDTO dependencies = reservaService.getReservaDependencies(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Dependências da reserva recuperadas com sucesso.", dependencies));
    }

    @GetMapping("/user/{usuarioId}")
    public ResponseEntity<ResponseDTO<List<VwTableReserva>>> getByUser(@PathVariable UUID usuarioId) {
        List<VwTableReserva> reservas = reservaService.getReservasByUsuario(usuarioId);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Reservas do usuário recuperadas com sucesso.", reservas));
    }
}
