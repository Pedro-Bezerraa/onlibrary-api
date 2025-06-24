package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.usuarioBiblioteca.UpdateUsuarioBibliotecaRequestDTO;
import com.onlibrary.onlibrary_api.dto.usuarioBiblioteca.UsuarioBibliotecaDependenciesDTO;
import com.onlibrary.onlibrary_api.dto.usuarioBiblioteca.UsuarioBibliotecaRequestDTO;
import com.onlibrary.onlibrary_api.dto.usuarioBiblioteca.UsuarioBibliotecaResponseDTO;
import com.onlibrary.onlibrary_api.exception.BusinessException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.Biblioteca;
import com.onlibrary.onlibrary_api.model.entities.PerfilUsuario;
import com.onlibrary.onlibrary_api.model.entities.Usuario;
import com.onlibrary.onlibrary_api.model.entities.UsuarioBiblioteca;
import com.onlibrary.onlibrary_api.model.enums.ContaSituacao;
import com.onlibrary.onlibrary_api.model.enums.SituacaoMulta;
import com.onlibrary.onlibrary_api.model.enums.TipoUsuario;
import com.onlibrary.onlibrary_api.model.views.VwTabelaUsuarioBiblioteca;
import com.onlibrary.onlibrary_api.repository.entities.*;
import com.onlibrary.onlibrary_api.repository.views.VwTabelaUsuarioBibliotecaRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final MultaRepository multaRepository;
    private final VwTabelaUsuarioBibliotecaRepository vwTabelaUsuarioBibliotecaRepository;

    @Transactional(readOnly = true)
    public List<VwTabelaUsuarioBiblioteca> searchUsuariosInBiblioteca(String value, String filter, UUID bibliotecaId) {
        if (value == null || value.trim().isEmpty()) {
            return vwTabelaUsuarioBibliotecaRepository.findByFkIdBiblioteca(bibliotecaId);
        }

        return switch (filter.toLowerCase()) {
            case "username" -> vwTabelaUsuarioBibliotecaRepository.searchByUsernameInBiblioteca(bibliotecaId, value);
            case "nome" -> vwTabelaUsuarioBibliotecaRepository.searchByNomeInBiblioteca(bibliotecaId, value);
            case "email" -> vwTabelaUsuarioBibliotecaRepository.searchByEmailInBiblioteca(bibliotecaId, value);
            case "cpf" -> vwTabelaUsuarioBibliotecaRepository.searchByCpfInBiblioteca(bibliotecaId, value);
            case "perfil" -> vwTabelaUsuarioBibliotecaRepository.searchByPerfilInBiblioteca(bibliotecaId, value);
            case "situação" -> vwTabelaUsuarioBibliotecaRepository.searchBySituacaoInBiblioteca(bibliotecaId, value);
            case "todos" -> vwTabelaUsuarioBibliotecaRepository.searchByAllInBiblioteca(bibliotecaId, value);
            default -> new ArrayList<>();
        };
    }

    @Transactional(readOnly = true)
    public UsuarioBibliotecaDependenciesDTO getUsuarioBibliotecaDependencies(UUID id) {
        UsuarioBiblioteca usuarioBiblioteca = usuarioBibliotecaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Relação Usuário-Biblioteca não encontrada."));

        var tipoUsuario = new UsuarioBibliotecaDependenciesDTO.LabelValue<>(
                usuarioBiblioteca.getTipoUsuario().toLower(),
                usuarioBiblioteca.getTipoUsuario().toLower()
        );

        var situacao = new UsuarioBibliotecaDependenciesDTO.LabelValue<>(
                usuarioBiblioteca.getSituacao().toLower(),
                usuarioBiblioteca.getSituacao().toLower()
        );

        var usuario = new UsuarioBibliotecaDependenciesDTO.LabelValue<>(
                usuarioBiblioteca.getUsuario().getUsername(),
                "" // Conforme solicitado, o valor para o usuário é vazio
        );

        var perfilAtual = new UsuarioBibliotecaDependenciesDTO.LabelValue<>(
                usuarioBiblioteca.getPerfilUsuario().getNome(),
                usuarioBiblioteca.getPerfilUsuario().getId()
        );

        return new UsuarioBibliotecaDependenciesDTO(
                usuarioBiblioteca.getNumeroMatricula(),
                usuarioBiblioteca.getCpf(),
                tipoUsuario,
                situacao,
                usuario,
                perfilAtual
        );
    }

    @Transactional
    public UsuarioBibliotecaResponseDTO criarUsuarioBiblioteca(UsuarioBibliotecaRequestDTO dto) {
        Biblioteca biblioteca = bibliotecaRepository.findById(dto.bibliotecaId())
                .orElseThrow(() -> new ResourceNotFoundException("Biblioteca não encontrada."));

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        PerfilUsuario perfilUsuario = perfilUsuarioRepository.findById(dto.perfilUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado."));

        boolean usuarioExisteNaBiblioteca = usuarioBibliotecaRepository.existsByUsuarioIdAndBibliotecaId(dto.usuarioId(), dto.bibliotecaId());

        if (usuarioExisteNaBiblioteca) {
            throw new BusinessException("Usuario já cadastrado nesta biblioteca");
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
    public UsuarioBibliotecaResponseDTO atualizarUsuarioBiblioteca(UpdateUsuarioBibliotecaRequestDTO dto, UUID id) {
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
            throw new BusinessException("Não é possível excluir a relação Usuário-Biblioteca: Existem reservas ativas associadas a ela.");
        }

        boolean hasPendingEmprestimos = usuarioBibliotecaRepository.hasPendingEmprestimosByUsuarioBibliotecaId(idUsuarioBiblioteca);
        if (hasPendingEmprestimos) {
            throw new BusinessException("Não é possível excluir a relação Usuário-Biblioteca: Existem empréstimos pendentes associados a ela.");
        }

        boolean hasPendingMultas = multaRepository.existsByUsuarioIdAndBibliotecaIdAndSituacao(
                usuarioBiblioteca.getUsuario().getId(),
                usuarioBiblioteca.getBiblioteca().getId(),
                SituacaoMulta.PENDENTE
        );

        if (hasPendingMultas) {
            throw new BusinessException("Não é possível excluir a relação Usuário-Biblioteca: Existem multas pendentes associadas a este usuário na biblioteca.");
        }

        if (usuarioBiblioteca.getTipoUsuario() == TipoUsuario.ADMIN_MASTER) {
            long adminMastersCount = usuarioBibliotecaRepository.findByUsuarioIdAndTipoUsuarioIn(
                            usuarioBiblioteca.getUsuario().getId(), List.of(TipoUsuario.ADMIN_MASTER))
                    .stream()
                    .filter(ub -> !ub.getId().equals(idUsuarioBiblioteca) && !ub.getDeletado())
                    .count();

            if (adminMastersCount == 0 && !usuarioBiblioteca.getDeletado()) {
                throw new BusinessException("Não é possível excluir a relação Usuário-Biblioteca: Este usuário é o dono ativo da biblioteca.");
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
