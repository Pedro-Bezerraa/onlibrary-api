package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.LoginRequestDTO;
import com.onlibrary.onlibrary_api.dto.RegisterRequestDTO;
import com.onlibrary.onlibrary_api.dto.RegisterResponseDTO;
import com.onlibrary.onlibrary_api.dto.TokenDTO;
import com.onlibrary.onlibrary_api.exception.ConflictException;
import com.onlibrary.onlibrary_api.exception.InvalidCredentialsException;
import com.onlibrary.onlibrary_api.model.ContaSituacao;
import com.onlibrary.onlibrary_api.model.Usuario;
import com.onlibrary.onlibrary_api.model.UsuarioRole;
import com.onlibrary.onlibrary_api.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        novoUsuario.setRole(UsuarioRole.ROLE_USUARIO);


        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);

        return RegisterResponseDTO.builder()
                .nome(usuarioSalvo.getNome())
                .sobrenome(usuarioSalvo.getSobrenome())
                .username(usuarioSalvo.getUsername())
                .email(usuarioSalvo.getEmail())
                .cpf(usuarioSalvo.getCpf())
                .situacao(usuarioSalvo.getSituacao())
                .role(usuarioSalvo.getRole())
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
}
