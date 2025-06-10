package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.usuarioBiblioteca.AttUsuarioBibliotecaRequestDTO;
import com.onlibrary.onlibrary_api.dto.usuarioBiblioteca.UsuarioBibliotecaRequestDTO;
import com.onlibrary.onlibrary_api.dto.usuarioBiblioteca.UsuarioBibliotecaResponseDTO;
import com.onlibrary.onlibrary_api.exception.BusinessException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.Biblioteca;
import com.onlibrary.onlibrary_api.model.entities.PerfilUsuario;
import com.onlibrary.onlibrary_api.model.entities.Usuario;
import com.onlibrary.onlibrary_api.model.entities.UsuarioBiblioteca;
import com.onlibrary.onlibrary_api.model.enums.ContaSituacao;
import com.onlibrary.onlibrary_api.model.enums.TipoUsuario;
import com.onlibrary.onlibrary_api.repository.BibliotecaRepository;
import com.onlibrary.onlibrary_api.repository.PerfilUsuarioRepository;
import com.onlibrary.onlibrary_api.repository.UsuarioBibliotecaRepository;
import com.onlibrary.onlibrary_api.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioBibliotecaService {
    private final UsuarioBibliotecaRepository usuarioBibliotecaRepository;
    private final BibliotecaRepository bibliotecaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilUsuarioRepository perfilUsuarioRepository;
    private final NotificacaoService notificacaoService;

    @Transactional
    public UsuarioBibliotecaResponseDTO criarUsuarioBiblioteca(UsuarioBibliotecaRequestDTO dto) {
        Biblioteca biblioteca = bibliotecaRepository.findById(dto.bibliotecaId())
                .orElseThrow(() -> new ResourceNotFoundException("Biblioteca não encontrada."));

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        PerfilUsuario perfilUsuario = perfilUsuarioRepository.findById(dto.perfilUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado."));

        boolean usuarioExiste = usuarioBibliotecaRepository.existsByUsuarioId(dto.usuarioId());

        if (usuarioExiste) {
            throw new BusinessException("Usuario já cadastrado na biblioteca");
        }

        if (!usuario.getCpf().equals(dto.cpf())) {
            throw new BusinessException("O CPF informado não corresponde ao CPF do usuário.");
        }

        String nomePerfil = perfilUsuario.getNome().toLowerCase();

        if (dto.tipoUsuario() == TipoUsuario.COMUM && nomePerfil.equalsIgnoreCase("bibliotecario")) {
            throw new BusinessException("Usuários do tipo COMUM não podem ter o perfil de BIBLIOTECARIO.");
        }

        if (dto.tipoUsuario() == TipoUsuario.ADMIN && !nomePerfil.equalsIgnoreCase("bibliotecario")) {
            throw new BusinessException("Usuários do tipo ADMIN só podem ter o perfil de BIBLIOTECARIO.");
        }

        if (nomePerfil.equalsIgnoreCase("bibliotecario") && dto.tipoUsuario() != TipoUsuario.ADMIN) {
            throw new BusinessException("Perfis de BIBLIOTECARIO devem ser do tipo ADMIN.");
        }

        if (nomePerfil.equalsIgnoreCase("outro") && dto.tipoUsuario() != TipoUsuario.COMUM) {
            throw new BusinessException("Perfis de OUTRO devem ser do tipo COMUM.");
        }

        UsuarioBiblioteca usuarioBiblioteca = new UsuarioBiblioteca();
        usuarioBiblioteca.setBiblioteca(biblioteca);
        usuarioBiblioteca.setUsuario(usuario);
        usuarioBiblioteca.setPerfilUsuario(perfilUsuario);
        usuarioBiblioteca.setTipoUsuario(dto.tipoUsuario());
        usuarioBiblioteca.setNumeroMatricula(dto.numeroMatricula());
        usuarioBiblioteca.setCpf(dto.cpf());
        usuarioBiblioteca.setSituacao(ContaSituacao.ATIVO);

        usuarioBibliotecaRepository.save(usuarioBiblioteca);

        return new UsuarioBibliotecaResponseDTO(
                usuarioBiblioteca.getId(),
                usuarioBiblioteca.getBiblioteca().getId(),
                usuarioBiblioteca.getUsuario().getId(),
                usuarioBiblioteca.getPerfilUsuario().getId(),
                usuarioBiblioteca.getTipoUsuario().toString(),
                usuarioBiblioteca.getNumeroMatricula(),
                usuarioBiblioteca.getCpf(),
                usuarioBiblioteca.getSituacao().toString()
        );
    }

    @Transactional
    public UsuarioBibliotecaResponseDTO atualizarUsuarioBiblioteca(AttUsuarioBibliotecaRequestDTO dto, UUID id) {
        UsuarioBiblioteca usuarioBiblioteca = usuarioBibliotecaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Relação Usuário-Biblioteca não encontrada."));

        if (dto.perfilUsuarioId() != null) {
            PerfilUsuario perfilUsuario = perfilUsuarioRepository.findById(dto.perfilUsuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado."));

            String nomePerfil = perfilUsuario.getNome().toLowerCase();

            if (dto.tipoUsuario() != null) {
                if (dto.tipoUsuario() == TipoUsuario.COMUM && nomePerfil.equalsIgnoreCase("bibliotecario")) {
                    throw new BusinessException("Usuários do tipo COMUM não podem ter o perfil de BIBLIOTECARIO.");
                }

                if (dto.tipoUsuario() == TipoUsuario.ADMIN && nomePerfil.equalsIgnoreCase("outro")) {
                    throw new BusinessException("Usuários do tipo ADMIN não podem ter o perfil de OUTRO.");
                }

                if (nomePerfil.equalsIgnoreCase("bibliotecario") && dto.tipoUsuario() != TipoUsuario.ADMIN) {
                    throw new BusinessException("Perfis de BIBLIOTECARIO devem ser do tipo ADMIN.");
                }
            }

            usuarioBiblioteca.setPerfilUsuario(perfilUsuario);
        }

        if (dto.tipoUsuario() != null) {
            usuarioBiblioteca.setTipoUsuario(dto.tipoUsuario());
        }

        if (dto.numeroMatricula() != null) {
            usuarioBiblioteca.setNumeroMatricula(dto.numeroMatricula());
        }

        if (dto.situacao() != null) {
            usuarioBiblioteca.setSituacao(dto.situacao());
        }

        usuarioBibliotecaRepository.save(usuarioBiblioteca);

        return new UsuarioBibliotecaResponseDTO(
                usuarioBiblioteca.getId(),
                usuarioBiblioteca.getBiblioteca().getId(),
                usuarioBiblioteca.getUsuario().getId(),
                usuarioBiblioteca.getPerfilUsuario().getId(),
                usuarioBiblioteca.getTipoUsuario().toString(),
                usuarioBiblioteca.getNumeroMatricula(),
                usuarioBiblioteca.getCpf(),
                usuarioBiblioteca.getSituacao().toString()
        );
    }

    @Transactional
    public void deletarUsuarioBiblioteca(UUID idUsuarioBiblioteca) {
        UsuarioBiblioteca usuarioBiblioteca = usuarioBibliotecaRepository.findById(idUsuarioBiblioteca)
                .orElseThrow(() -> new ResourceNotFoundException("Relação Usuário-Biblioteca não encontrada."));

        boolean hasActiveReservas = usuarioBibliotecaRepository.hasActiveReservasByUsuarioBibliotecaId(idUsuarioBiblioteca);
        if (hasActiveReservas) {
            notificacaoService.notificarUsuario(
                    usuarioBiblioteca.getUsuario(),
                    "Não foi possível excluir a relação Usuário-Biblioteca",
                    "A relação com a biblioteca '" + usuarioBiblioteca.getBiblioteca().getNome() +
                            "' não pode ser excluída pois existem reservas ativas associadas a ela.",
                    usuarioBiblioteca.getTipoUsuario()
            );
            throw new BusinessException("Não é possível excluir a relação Usuário-Biblioteca: Existem reservas ativas associadas a ela.");
        }

        boolean hasPendingEmprestimos = usuarioBibliotecaRepository.hasPendingEmprestimosByUsuarioBibliotecaId(idUsuarioBiblioteca);
        if (hasPendingEmprestimos) {
            notificacaoService.notificarUsuario(
                    usuarioBiblioteca.getUsuario(),
                    "Não foi possível excluir a relação Usuário-Biblioteca",
                    "A relação com a biblioteca '" + usuarioBiblioteca.getBiblioteca().getNome() +
                            "' não pode ser excluída pois existem empréstimos pendentes associados a ela.",
                    usuarioBiblioteca.getTipoUsuario()
            );
            throw new BusinessException("Não é possível excluir a relação Usuário-Biblioteca: Existem empréstimos pendentes associados a ela.");
        }

        if (usuarioBiblioteca.getTipoUsuario() == TipoUsuario.ADMIN_MASTER) {
            long adminMastersCount = usuarioBibliotecaRepository.findByUsuarioIdAndTipoUsuarioIn(
                            usuarioBiblioteca.getUsuario().getId(), List.of(TipoUsuario.ADMIN_MASTER))
                    .stream()
                    .filter(ub -> !ub.getId().equals(idUsuarioBiblioteca) && !ub.getDeletado())
                    .count();

            if (adminMastersCount == 0 && !usuarioBiblioteca.getDeletado()) { // Se ele for o único ADMIN_MASTER ativo
                throw new BusinessException("Não é possível excluir a relação Usuário-Biblioteca: Este usuário é o único ADMIN_MASTER ativo da biblioteca. É necessário transferir ou adicionar outro ADMIN_MASTER antes de excluí-lo.");
            }
        }


        usuarioBiblioteca.setDeletado(true);
        usuarioBibliotecaRepository.save(usuarioBiblioteca);

        notificacaoService.notificarUsuario(
                usuarioBiblioteca.getUsuario(),
                "Relação com a biblioteca encerrada",
                "Sua relação com a biblioteca '" + usuarioBiblioteca.getBiblioteca().getNome() +
                        "' foi encerrada com sucesso.",
                usuarioBiblioteca.getTipoUsuario()
        );
    }
}
