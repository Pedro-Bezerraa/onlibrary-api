package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.multa.MultaDependenciesDTO;
import com.onlibrary.onlibrary_api.dto.multa.UpdateMultaRequestDTO;
import com.onlibrary.onlibrary_api.dto.multa.UpdateMultaResponseDTO;
import com.onlibrary.onlibrary_api.dto.multa.MultaRequestDTO;
import com.onlibrary.onlibrary_api.dto.multa.MultaResponseDTO;
import com.onlibrary.onlibrary_api.exception.BusinessException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.*;
import com.onlibrary.onlibrary_api.model.enums.SituacaoMulta;
import com.onlibrary.onlibrary_api.model.views.VwTableMulta;
import com.onlibrary.onlibrary_api.repository.entities.BibliotecaRepository;
import com.onlibrary.onlibrary_api.repository.entities.MultaRepository;
import com.onlibrary.onlibrary_api.repository.entities.UsuarioBibliotecaRepository;
import com.onlibrary.onlibrary_api.repository.entities.UsuarioRepository;
import com.onlibrary.onlibrary_api.repository.views.VwTableMultaRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MultaService {
    private final MultaRepository multaRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioBibliotecaRepository usuarioBibliotecaRepository;
    private final BibliotecaRepository bibliotecaRepository;
    private final NotificacaoService notificacaoService;
    private final VwTableMultaRepository vwTableMultaRepository;

    @Transactional(readOnly = true)
    public List<VwTableMulta> searchMultas(String value, String filter, UUID bibliotecaId) {
        if (value == null || value.trim().isEmpty()) {
            return vwTableMultaRepository.findByFkIdBiblioteca(bibliotecaId);
        }

        return switch (filter.toLowerCase()) {
            case "username" -> vwTableMultaRepository.searchByUsernameInBiblioteca(bibliotecaId, value);
            case "situação" -> vwTableMultaRepository.searchBySituacaoInBiblioteca(bibliotecaId, value);
            case "todos" -> vwTableMultaRepository.searchByAllInBiblioteca(bibliotecaId, value);
            default -> new ArrayList<>();
        };
    }

    @Transactional(readOnly = true)
    public MultaDependenciesDTO getMultaDependencies(UUID id) {
        Multa multa = multaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Multa não encontrada."));

        Usuario usuario = multa.getUsuario();
//
//        var situacao = new MultaDependenciesDTO.LabelValue<>(
//                multa.getSituacao().toLower(),
//                multa.getSituacao().toLower()
//        );

        var usuarioInfo = new MultaDependenciesDTO.LabelValue<>(
                usuario.getUsername(),
                usuario.getId()
        );

        return new MultaDependenciesDTO(
                multa.getValor(),
                multa.getMotivo(),
                multa.getDataVencimento(),
                multa.getSituacao(),
                usuarioInfo
        );
    }

    @Transactional(readOnly = true)
    public List<VwTableMulta> getMultasByUsuario(UUID usuarioId) {
        return vwTableMultaRepository.findByFkIdUsuario(usuarioId);
    }

    public MultaResponseDTO cadastrarMulta(MultaRequestDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Usuario bibliotecario = usuarioRepository.findById(dto.bibliotecarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Bibliotecário não encontrado"));

        Biblioteca biblioteca = bibliotecaRepository.findById(dto.bibliotecaId())
                .orElseThrow(() -> new ResourceNotFoundException("Biblioteca não encontrada."));

        UsuarioBiblioteca usuarioBiblioteca = usuarioBibliotecaRepository
                .findByUsuarioIdAndBibliotecaId(dto.usuarioId(), dto.bibliotecaId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado na biblioteca."));

        PerfilUsuario perfil = usuarioBiblioteca.getPerfilUsuario();

        LocalDate dataEmissao = LocalDate.now();
        LocalDate dataVencimento = dataEmissao.plusDays(perfil.getPrazoMultaPadrao());

        Multa multa = new Multa();

        multa.setUsuario(usuario);
        multa.setBibliotecario(bibliotecario);
        multa.setValor(perfil.getMultaPadrao());
        multa.setDataEmissao(dataEmissao);
        multa.setDataVencimento(dataVencimento);
        multa.setSituacao(SituacaoMulta.PENDENTE);
        multa.setMotivo(dto.motivo());
        multa.setBiblioteca(biblioteca);
        multa.setEmprestimo(null);

        multaRepository.save(multa);

        return new MultaResponseDTO(multa.getId(), multa.getUsuario().getId(), multa.getBibliotecario().getId(), multa.getValor(), multa.getDataEmissao(), multa.getDataVencimento(), multa.getSituacao(), multa.getMotivo(), multa.getBiblioteca().getId(), null);
    }

    public UpdateMultaResponseDTO atualizarMulta(UUID multaId, UpdateMultaRequestDTO dto) {
        Multa multa = multaRepository.findById(multaId)
                .orElseThrow(() -> new ResourceNotFoundException("Multa não encontrada."));

        if (multa.getSituacao() == SituacaoMulta.CONCLUIDO || multa.getSituacao() == SituacaoMulta.CANCELADO) {
            throw new BusinessException("Não é possível atualizar multa já finalizada.");
        }

        if (dto.motivo() != null) {
            multa.setMotivo(dto.motivo());
        }

        if (dto.situacao() != null) {
            multa.setSituacao(dto.situacao());
        }

        if (dto.bibliotecarioId() != null) {
            Usuario novoBibliotecario = usuarioRepository.findById(dto.bibliotecarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Novo bibliotecário não encontrado."));
            multa.setBibliotecario(novoBibliotecario);
        }

        multaRepository.save(multa);

        return new UpdateMultaResponseDTO(
                multa.getId(),
                multa.getUsuario().getId(),
                multa.getBibliotecario().getId(),
                multa.getValor(),
                multa.getDataEmissao(),
                multa.getDataVencimento(),
                multa.getSituacao(),
                multa.getMotivo(),
                multa.getBiblioteca().getId(),
                null
        );
    }

    @Transactional
    public void deletarMulta(UUID idMulta) {
        Multa multa = multaRepository.findById(idMulta)
                .orElseThrow(() -> new ResourceNotFoundException("Multa não encontrada."));

        if (multa.getSituacao() == SituacaoMulta.PENDENTE) {
//            notificacaoService.notificarUsuario(
//                    multa.getBibliotecario(),
//                    "Não foi possível excluir a multa",
//                    "A multa para o usuário '" + multa.getUsuario().getUsername() +
//                            "' na biblioteca '" + multa.getBiblioteca().getNome() +
//                            "' não pode ser excluída pois está com status PENDENTE.",
//                    TipoUsuario.ADMIN
//            );
            throw new BusinessException("Não é possível excluir uma multa pendente.");
        }

        multa.setDeletado(true);
        multaRepository.save(multa);

//        notificacaoService.notificarUsuario(
//                multa.getUsuario(),
//                "Multa arquivada",
//                "Sua multa no valor de " + multa.getValor() +
//                        " na biblioteca '" + multa.getBiblioteca().getNome() +
//                        "' foi arquivada do sistema. Não há mais pendências associadas a ela.",
//                TipoUsuario.COMUM
//        );
    }
}
