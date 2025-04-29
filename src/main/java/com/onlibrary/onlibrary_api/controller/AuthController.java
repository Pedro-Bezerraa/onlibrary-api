package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.*;
import com.onlibrary.onlibrary_api.repository.UsuarioRepository;
import com.onlibrary.onlibrary_api.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUsuario(@Valid @RequestBody RegisterRequestDTO requestDTO) {
       RegisterResponseDTO usuarioCriado = authService.registerUsuario(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(true, "Usuário cadastrado com sucesso!", usuarioCriado));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        TokenDTO tokenDTO = authService.login(loginRequestDTO);

        ResponseCookie cookie = ResponseCookie.from("jwt", tokenDTO.getAccessToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .sameSite("None")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().body(new ResponseDTO<>(true, "Autenticação realizada com sucesso!", tokenDTO));
    }

    @PostMapping("/validar-etapa")
    public ResponseEntity<?> validarEtapa(@RequestBody EtapaCadastroRequestDTO request) {
        int etapa = request.getEtapa();
        Map<String, String> dados = request.getDados();

        switch (etapa) {
            case 1:
                return validarEtapa1(dados);
            case 2:
                return validarEtapa2(dados);
            default:
                return ResponseEntity.badRequest().body("Etapa inválida");
        }
    }

    private ResponseEntity<?> validarEtapa1(Map<String, String> dados) {
        String nome = dados.get("nome");
        String sobrenome = dados.get("sobrenome");
        String email = dados.get("email");

        if (nome == null || nome.isBlank()) {
            return ResponseEntity.badRequest().body("Nome é obrigatório");
        }

        if (sobrenome == null || sobrenome.isBlank()) {
            return ResponseEntity.badRequest().body("Sobrenome é obrigatório");
        }

        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("Email é obrigatório");
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return ResponseEntity.badRequest().body("Email inválido");
        }
//
        if (usuarioRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body("Email já cadastrado");
        }
        return ResponseEntity.ok(new ResponseDTO<>(true, "Dados válidos", dados));
    }

    private ResponseEntity<?> validarEtapa2(Map<String, String> dados) {
        String username = dados.get("username");
        String cpf = dados.get("cpf");

        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().body("Username é obrigatório");
        }

        if (!username.matches("^[a-zA-Z0-9._-]{3,}$")) {
            return ResponseEntity.badRequest().body("Username inválido");
        }

        if (usuarioRepository.existsByUsername(username)) {
            return ResponseEntity.badRequest().body("Username já está em uso");
        }

        if (cpf == null || !cpf.matches("\\d{11}")) {
            return ResponseEntity.badRequest().body("CPF inválido");
        }

        if (usuarioRepository.existsByCpf(cpf)) {
            return ResponseEntity.badRequest().body("CPF já cadastrado");
        }
        return ResponseEntity.ok(new ResponseDTO<>(true, "Dados válidos", dados));
    }
}
