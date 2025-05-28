package com.onlibrary.onlibrary_api.controller;

import com.onlibrary.onlibrary_api.dto.ResponseDTO;
import com.onlibrary.onlibrary_api.dto.usuario.*;
import com.onlibrary.onlibrary_api.repository.UsuarioRepository;
import com.onlibrary.onlibrary_api.service.AuthService;
import com.onlibrary.onlibrary_api.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {
        TokenDTO tokenDTO = authService.login(loginRequestDTO);

//        ResponseCookie cookie = ResponseCookie.from("jwt", tokenDTO.getAccessToken())
//                .httpOnly(true)
//                .secure(false)
//                .path("/")
//                .sameSite("None")
//                .maxAge(7 * 24 * 60 * 60)
//                .build();
//
//        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok().body(new ResponseDTO<>(true, "Autenticação realizada com sucesso!", tokenDTO));
    }

    @PostMapping("/validar-etapa")
    public ResponseEntity<?> validarEtapa(@RequestBody EtapaCadastroRequestDTO request) {
        authService.validarEtapa(request.getEtapa(), request.getDados());
        return ResponseEntity.ok(new ResponseDTO<>(true, "Dados válidos", request.getDados()));
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
