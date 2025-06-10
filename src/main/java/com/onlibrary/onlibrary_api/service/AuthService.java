package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.usuario.*;
import com.onlibrary.onlibrary_api.exception.*;
import com.onlibrary.onlibrary_api.model.entities.Usuario;
import com.onlibrary.onlibrary_api.model.enums.ContaSituacao;
import com.onlibrary.onlibrary_api.model.enums.TipoUsuario;
import com.onlibrary.onlibrary_api.repository.UsuarioRepository;
import com.onlibrary.onlibrary_api.security.UsuarioDetails;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final NotificacaoService notificacaoService;

    @Transactional
    public UsuarioResponseDTO registerUsuario(UsuarioRequestDTO requestDTO) {
        if (usuarioRepository.existsByUsername(requestDTO.username()) || usuarioRepository.existsByEmail(requestDTO.email())) {
            throw new ConflictException("Nome de usuário ou Email já em uso.");
        }

        Usuario novoUsuario = new Usuario();

        novoUsuario.setNome(requestDTO.nome());
        novoUsuario.setSobrenome(requestDTO.sobrenome());
        novoUsuario.setUsername(requestDTO.username());
        novoUsuario.setEmail(requestDTO.email());
        novoUsuario.setCpf(requestDTO.cpf());
        novoUsuario.setSenha(passwordEncoder.encode(requestDTO.senha()));
        novoUsuario.setSituacao(ContaSituacao.ATIVO);
        Usuario usuarioSalvo = usuarioRepository.save(novoUsuario);

        return new UsuarioResponseDTO(novoUsuario.getNome(), novoUsuario.getSobrenome(), novoUsuario.getUsername(), novoUsuario.getEmail(), novoUsuario.getCpf(), novoUsuario.getSituacao());
    }

    @Transactional
    public AttUsuarioResponseDTO atualizarUsuario(UUID id, AttUsuarioRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (dto.username() != null && !dto.username().equals(usuario.getUsername())) {
            if (usuarioRepository.existsByUsernameAndIdNot(dto.username(), id)) {
                throw new ConflictException("Nome de usuário já em uso.");
            }
            usuario.setUsername(dto.username());
        }

        if (dto.email() != null && !dto.email().equals(usuario.getEmail())) {
            if (usuarioRepository.existsByEmailAndIdNot(dto.email(), id)) {
                throw new ConflictException("Email já em uso.");
            }
            usuario.setEmail(dto.email());
        }

        if (dto.cpf() != null && !dto.cpf().equals(usuario.getCpf())) {
            if (usuarioRepository.existsByCpfAndIdNot(dto.cpf(), id)) {
                throw new ConflictException("CPF já em uso.");
            }
            usuario.setCpf(dto.cpf());
        }

        if (dto.nome() != null) usuario.setNome(dto.nome());
        if (dto.sobrenome() != null) usuario.setSobrenome(dto.sobrenome());

        Usuario atualizado = usuarioRepository.save(usuario);

        return new AttUsuarioResponseDTO(
                atualizado.getNome(),
                atualizado.getSobrenome(),
                atualizado.getUsername(),
                atualizado.getEmail(),
                atualizado.getCpf(),
                atualizado.getSituacao()
        );
    }

    @Transactional
    public void deletarUsuario(UUID idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        boolean hasActiveReservas = usuarioRepository.hasActiveReservas(idUsuario);
        boolean hasActiveEmprestimos = usuarioRepository.hasActiveEmprestimos(idUsuario);
        boolean hasActiveMultas = usuarioRepository.hasActiveMultas(idUsuario);

        if (hasActiveReservas) {
            throw new BusinessException("Não é possível excluir o usuário: Existem reservas ativas associadas a ele.");
        }
        if (hasActiveEmprestimos) {
            throw new BusinessException("Não é possível excluir o usuário: Existem empréstimos pendentes associados a ele.");
        }
        if (hasActiveMultas) {
            throw new BusinessException("Não é possível excluir o usuário: Existem multas pendentes associadas a ele.");
        }

        boolean isAdminMaster = usuarioRepository.isAdminMasterOfAnyBiblioteca(idUsuario);

        if (isAdminMaster) {
            throw new BusinessException("Não é possível excluir sua conta no memento. Exclua sua biblioteca primeiro.");
        } else {
            notificacaoService.notificarUsuario(
                    usuario,
                    "Conta marcada para exclusão",
                    "Sua conta foi marcada para exclusão. Todos os seus dados serão anonimizados e você não terá mais acesso à plataforma.",
                    TipoUsuario.COMUM

            );
            usuario.setDeletado(true);
        }

        usuarioRepository.save(usuario);
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.login(),
                            loginRequestDTO.senha()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
            String accessToken = jwtService.generateAcessToken(auth);

            UsuarioDetails usuario = (UsuarioDetails) auth.getPrincipal();

            return new LoginResponseDTO(
                    accessToken,
                    usuario.getId(),
                    usuario.getUsername(),
                    usuario.getEmail()
            );
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Credênciais inválidas!");
        }
    }

    public void validarEtapa(int etapa, Map<String, String> dados) {
        switch (etapa) {
            case 1 -> validarEtapa1(dados);
            case 2 -> validarEtapa2(dados);
            default -> throw new EtapaInvalidaException("Etapa inválida.");
        }
    }

    private void validarEtapa1(Map<String, String> dados) {
        String nome = dados.get("nome");
        String sobrenome = dados.get("sobrenome");
        String cpf = dados.get("cpf");

        if (nome == null || nome.isBlank()) {
            throw new DadoObrigatorioException("Nome é obrigatório.");
        }

        if (sobrenome == null || sobrenome.isBlank()) {
            throw new DadoObrigatorioException("Sobrenome é obrigatório.");
        }

        if (cpf == null || !cpf.matches("\\d{11}")) {
            throw new DadoInvalidoException("CPF inválido.");
        }

        if (usuarioRepository.existsByCpf(cpf)) {
            throw new ConflictException("CPF já cadastrado.");
        }
    }

    private void validarEtapa2(Map<String, String> dados) {
        String username = dados.get("username");
        String email = dados.get("email");

        if (username == null || username.isBlank()) {
            throw new DadoObrigatorioException("Username é obrigatório.");
        }

        if (!username.matches("^[a-zA-Z0-9._-]{3,}$")) {
            throw new DadoInvalidoException("Username inválido.");
        }

        if (usuarioRepository.existsByUsername(username)) {
            throw new ConflictException("Username já está em uso.");
        }

        if (email == null || email.isBlank()) {
            throw new DadoObrigatorioException("Email é obrigatório.");
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new DadoInvalidoException("Email inválido.");
        }

        if (usuarioRepository.existsByEmail(email)) {
            throw new ConflictException("Email já cadastrado.");
        }
    }
}
