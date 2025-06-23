package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.emprestimo.EmprestimoDependenciesDTO;
import com.onlibrary.onlibrary_api.dto.emprestimo.UpdateEmprestimoRequestDTO;
import com.onlibrary.onlibrary_api.dto.emprestimo.EmprestimoRequestDTO;
import com.onlibrary.onlibrary_api.dto.emprestimo.EmprestimoResponseDTO;
import com.onlibrary.onlibrary_api.model.views.VwTableEmprestimo;
import com.onlibrary.onlibrary_api.service.EmprestimoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
            @RequestBody UpdateEmprestimoRequestDTO dto) {

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

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO<List<VwTableEmprestimo>>> search(
            @RequestParam("id_biblioteca") UUID bibliotecaId,
            @RequestParam(required = false) String value,
            @RequestParam(defaultValue = "todos") String filter) {
        List<VwTableEmprestimo> result = emprestimoService.searchEmprestimos(value, filter, bibliotecaId);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Pesquisa de empréstimos realizada com sucesso.", result));
    }

    @GetMapping("/dependencies/{id}")
    public ResponseEntity<ResponseDTO<EmprestimoDependenciesDTO>> getDependencies(@PathVariable UUID id) {
        EmprestimoDependenciesDTO dependencies = emprestimoService.getEmprestimoDependencies(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Dependências do empréstimo recuperadas com sucesso.", dependencies));
    }

    @GetMapping("/user/{usuarioId}")
    public ResponseEntity<ResponseDTO<List<VwTableEmprestimo>>> getByUser(@PathVariable UUID usuarioId) {
        List<VwTableEmprestimo> emprestimos = emprestimoService.getEmprestimosByUsuario(usuarioId);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Empréstimos do usuário recuperados com sucesso.", emprestimos));
    }
}
