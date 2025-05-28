package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.biblioteca.EmprestimoRequestDTO;
import com.onlibrary.onlibrary_api.service.BibliotecaService;
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

    @PutMapping("/atualizar-emprestimo/{id}")
    public ResponseEntity<?> atualizarEmprestimo(
            @PathVariable UUID id,
            @RequestBody EmprestimoRequestDTO dto) {

        emprestimoService.atualizarEmprestimo(id, dto);
        return ResponseEntity.ok("Empréstimo atualizado com sucesso.");
    }
}
