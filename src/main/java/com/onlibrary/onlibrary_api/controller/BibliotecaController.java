package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.biblioteca.*;
import com.onlibrary.onlibrary_api.model.views.VwTableEmprestimo;
import com.onlibrary.onlibrary_api.repository.views.VwTableEmprestimoRepository;
import com.onlibrary.onlibrary_api.service.BibliotecaService;
import com.onlibrary.onlibrary_api.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/biblioteca")
@RequiredArgsConstructor
@Slf4j
public class BibliotecaController {
    private final BibliotecaService bibliotecaService;
    private final JwtService jwtService;
    private final VwTableEmprestimoRepository vwTableEmprestimoRepository;

    @PostMapping("/criar-biblioteca")
    public ResponseEntity<ResponseDTO<Map<String, UUID>>> criarBiblioteca(
            @RequestBody @Valid BibliotecaRequestDTO dto,
            HttpServletRequest request
    ) {
        String token = jwtService.extractTokenFromRequest(request);

        UUID idBiblioteca = bibliotecaService.criarBiblioteca(dto, token);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO<>(true, "Biblioteca criada com sucesso", Map.of("idBiblioteca", idBiblioteca)));
    }

    @PutMapping("/atualizar-biblioteca/{id}")
    public ResponseEntity<?> atualizarBibioteca(@PathVariable UUID id, @RequestBody UpdateBibliotecaRequestDTO dto) {
        UpdateBibliotecaResponseDTO bibliotecaAtualizada = bibliotecaService.atualizarBiblioteca(id, dto);
        return ResponseEntity.ok()
                .body(new ResponseDTO<>(true, "Biblioteca atualizada com sucesso.", bibliotecaAtualizada));
    }

    @GetMapping("/minhas-bibliotecas")
    public ResponseEntity<ResponseDTO<List<BibliotecaResponseDTO>>> listarMinhasBibliotecas(HttpServletRequest request) {
        String token = jwtService.extractTokenFromRequest(request);

        List<BibliotecaResponseDTO> bibliotecas = bibliotecaService.listarBibliotecasAdminOuFuncionario(token);

        return ResponseEntity.ok(new ResponseDTO<>(true, "Bibliotecas recuperadas com sucesso", bibliotecas));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<ResponseDTO<Void>> deletarBiblioteca(@PathVariable UUID id) {
        bibliotecaService.deletarBiblioteca(id);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Biblioteca marcada como deletada com sucesso.", null));
    }

    @GetMapping("/contagem/{type}/{id}")
    public ResponseEntity<ContagemResponseDTO> contarPorBibliotecas(
            @PathVariable String type,
            @PathVariable UUID id) {
        ContagemResponseDTO response = bibliotecaService.contarPorBiblioteca(type, id);
        return ResponseEntity.ok(response);
    }

@GetMapping("/get")
    public List<VwTableEmprestimo> litarView() {
        return vwTableEmprestimoRepository.findByDataDevolucaoHoje();
    }
}
