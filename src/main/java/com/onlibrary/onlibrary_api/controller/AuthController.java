package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.*;
import com.onlibrary.onlibrary_api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUsuario(@Valid @RequestBody RegisterRequestDTO requestDTO) {
       RegisterResponseDTO usuarioCriado = authService.registerUsuario(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO<>(true, "Usuário cadastrado com sucesso!", usuarioCriado));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        TokenDTO tokenDTO = authService.login(loginRequestDTO);
        return ResponseEntity.ok(new ResponseDTO<>(true, "Autenticação realizada com sucesso!", tokenDTO));
    }

}
