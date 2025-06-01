package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.emprestimo.AttEmprestimoRequestDTO;
import com.onlibrary.onlibrary_api.dto.emprestimo.EmprestimoRequestDTO;
import com.onlibrary.onlibrary_api.model.entities.Reserva;
import com.onlibrary.onlibrary_api.service.EmprestimoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/emprestimo")
@RequiredArgsConstructor
public class EmprestimoController {
    private final EmprestimoService emprestimoService;

    @PostMapping("/criar-emprestimo")
    public ResponseEntity<?> criarEmprestimo(@RequestBody EmprestimoRequestDTO dto) {
        emprestimoService.criarEmprestimo(dto);
        return ResponseEntity.ok("Emprestimo realizado com sucesso");
    }

    @PutMapping("/atualizar-emprestimo/{id}")
    public ResponseEntity<?> atualizarEmprestimo(
            @PathVariable UUID id,
            @RequestBody AttEmprestimoRequestDTO dto) {

        emprestimoService.atualizarEmprestimo(id, dto);
        return ResponseEntity.ok("Empréstimo atualizado com sucesso.");
    }
}
