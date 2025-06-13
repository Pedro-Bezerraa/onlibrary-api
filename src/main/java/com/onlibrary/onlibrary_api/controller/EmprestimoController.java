package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.emprestimo.AttEmprestimoRequestDTO;
import com.onlibrary.onlibrary_api.dto.emprestimo.EmprestimoRequestDTO;
import com.onlibrary.onlibrary_api.dto.emprestimo.EmprestimoResponseDTO;
import com.onlibrary.onlibrary_api.model.entities.Reserva;
import com.onlibrary.onlibrary_api.service.EmprestimoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        EmprestimoResponseDTO emprestimo = emprestimoService.criarEmprestimo(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(true, "Empréstimo realizado com sucesso!", emprestimo));
    }

    @PutMapping("/atualizar-emprestimo/{id}")
    public ResponseEntity<?> atualizarEmprestimo(
            @PathVariable UUID id,
            @RequestBody AttEmprestimoRequestDTO dto) {

        EmprestimoResponseDTO emprestimoAtualizado = emprestimoService.atualizarEmprestimo(id, dto);

        return ResponseEntity.ok(
                new ResponseDTO<>(true, "Empréstimo atualizado com sucesso!", emprestimoAtualizado)
        );
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<ResponseDTO<Void>> deletarEmprestimo(@PathVariable UUID id) {
        emprestimoService.deletarEmprestimo(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Empréstimo marcado como deletado com sucesso.", null));
    }
}
