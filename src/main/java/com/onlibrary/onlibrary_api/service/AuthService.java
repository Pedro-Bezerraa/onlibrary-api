package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.usuario.LoginRequestDTO;
import com.onlibrary.onlibrary_api.dto.usuario.RegisterRequestDTO;
import com.onlibrary.onlibrary_api.dto.usuario.RegisterResponseDTO;
import com.onlibrary.onlibrary_api.dto.usuario.TokenDTO;
import com.onlibrary.onlibrary_api.exception.*;
import com.onlibrary.onlibrary_api.model.entities.Usuario;
import com.onlibrary.onlibrary_api.model.enums.ContaSituacao;
import com.onlibrary.onlibrary_api.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public RegisterResponseDTO registerUsuario(RegisterRequestDTO requestDTO) {
        if (usuarioRepository.existsByUsername(requestDTO.getUsername()) || usuarioRepository.existsByEmail(requestDTO.getEmail())) {
            throw new ConflictException("Nome de usuário ou Email já em uso");
        }

        Usuario novoUsuario = new Usuario();

        novoUsuario.setNome(requestDTO.getNome());
        novoUsuario.setSobrenome(requestDTO.getSobrenome());
        novoUsuario.setUsername(requestDTO.getUsername());
        novoUsuario.setEmail(requestDTO.getEmail());
        novoUsuario.setCpf(requestDTO.getCpf());
        novoUsuario.setSenha(passwordEncoder.encode(requestDTO.getSenha()));
        novoUsuario.setSituacao(ContaSituacao.ATIVO);


        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);

        return RegisterResponseDTO.builder()
                .nome(usuarioSalvo.getNome())
                .sobrenome(usuarioSalvo.getSobrenome())
                .username(usuarioSalvo.getUsername())
                .email(usuarioSalvo.getEmail())
                .cpf(usuarioSalvo.getCpf())
                .situacao(usuarioSalvo.getSituacao())
                .build();
    }

    public TokenDTO login(LoginRequestDTO loginRequestDTO) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getLogin(),
                            loginRequestDTO.getSenha()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            String accessToken = jwtService.generateAcessToken(auth);
            return new TokenDTO(accessToken);
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Credênciais inválidas!");
        }
    }

    public void validarEtapa(int etapa, Map<String, String> dados) {
        switch (etapa) {
            case 1 -> validarEtapa1(dados);
            case 2 -> validarEtapa2(dados);
            default -> throw new EtapaInvalidaException("Etapa inválida");
        }
    }

    private void validarEtapa1(Map<String, String> dados) {
        String nome = dados.get("nome");
        String sobrenome = dados.get("sobrenome");
        String cpf = dados.get("cpf");

        if (nome == null || nome.isBlank()) {
            throw new DadoObrigatorioException("Nome é obrigatório");
        }

        if (sobrenome == null || sobrenome.isBlank()) {
            throw new DadoObrigatorioException("Sobrenome é obrigatório");
        }

        if (cpf == null || !cpf.matches("\\d{11}")) {
            throw new DadoInvalidoException("CPF inválido");
        }

        if (usuarioRepository.existsByCpf(cpf)) {
            throw new ConflictException("CPF já cadastrado");
        }
    }

    private void validarEtapa2(Map<String, String> dados) {
        String username = dados.get("username");
        String email = dados.get("email");

        if (username == null || username.isBlank()) {
            throw new DadoObrigatorioException("Username é obrigatório");
        }

        if (!username.matches("^[a-zA-Z0-9._-]{3,}$")) {
            throw new DadoInvalidoException("Username inválido");
        }

        if (usuarioRepository.existsByUsername(username)) {
            throw new ConflictException("Username já está em uso");
        }

        if (email == null || email.isBlank()) {
            throw new DadoObrigatorioException("Email é obrigatório");
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new DadoInvalidoException("Email inválido");
        }

        if (usuarioRepository.existsByEmail(email)) {
            throw new ConflictException("Email já cadastrado");
        }
    }
}
