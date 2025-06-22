package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.perfilUsuario.UpdatePerfilUsuarioRequestDTO;
import com.onlibrary.onlibrary_api.dto.perfilUsuario.PerfilUsuarioRequestDTO;
import com.onlibrary.onlibrary_api.dto.perfilUsuario.PerfilUsuarioResponseDTO;
import com.onlibrary.onlibrary_api.exception.BusinessException;
import com.onlibrary.onlibrary_api.exception.ConflictException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.Biblioteca;
import com.onlibrary.onlibrary_api.model.entities.PerfilUsuario;
import com.onlibrary.onlibrary_api.repository.entities.BibliotecaRepository;
import com.onlibrary.onlibrary_api.repository.entities.PerfilUsuarioRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PerfilService {
    private final PerfilUsuarioRepository perfilUsuarioRepository;
    private final BibliotecaRepository bibliotecaRepository;

    @Transactional
    public PerfilUsuarioResponseDTO criarPerfil(PerfilUsuarioRequestDTO dto) {

        if (dto.multaPadrao() < 0) {
            throw new BusinessException("Multa padrão não pode ser negativa.");
        }

        if (dto.prazoMultaPadrao() < 0) {
            throw new BusinessException("Prazo de multa padrão não pode ser negativo.");
        }

        if (dto.prazoDevolucaoPadrao() < 0) {
            throw new BusinessException("Prazo de devolução padrão não pode ser negativo.");
        }

        boolean nomeExiste = perfilUsuarioRepository.existsByNomeAndBibliotecaId(dto.nome(), dto.bibliotecaId());
        if (nomeExiste) {
            throw new ConflictException("Já existe um perfil com esse nome nessa biblioteca.");
        }

        Biblioteca biblioteca = bibliotecaRepository.findById(dto.bibliotecaId())
                .orElseThrow(() -> new ResourceNotFoundException("Biblioteca não encontrada."));

        PerfilUsuario perfil = new PerfilUsuario();
        perfil.setBiblioteca(biblioteca);
        perfil.setNome(dto.nome());
        perfil.setMultaPadrao(dto.multaPadrao());
        perfil.setPrazoDevolucaoPadrao(dto.prazoDevolucaoPadrao());
        perfil.setPrazoMultaPadrao(dto.prazoMultaPadrao());

        perfilUsuarioRepository.save(perfil);

        return new PerfilUsuarioResponseDTO(
                perfil.getId(),
                perfil.getNome(),
                perfil.getMultaPadrao(),
                perfil.getPrazoDevolucaoPadrao(),
                perfil.getPrazoMultaPadrao(),
                perfil.getBiblioteca().getId()
        );
    }

    @Transactional
    public PerfilUsuarioResponseDTO atualizarPerfil(UpdatePerfilUsuarioRequestDTO dto, UUID id) {
        PerfilUsuario perfil = perfilUsuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado."));

        if (dto.nome() != null) {
            boolean nomeExiste = perfilUsuarioRepository.existsByNomeAndBibliotecaIdAndIdNot(
                    dto.nome(), perfil.getBiblioteca().getId(), id);
            if (nomeExiste) {
                throw new ConflictException("Já existe um perfil com esse nome nessa biblioteca.");
            }
            perfil.setNome(dto.nome());
        }

        if (dto.multaPadrao() != null) {
            if (dto.multaPadrao() < 0) {
                throw new BusinessException("Multa padrão não pode ser negativa.");
            }
            perfil.setMultaPadrao(dto.multaPadrao());
        }

        if (dto.prazoMultaPadrao() != null) {
            if (dto.prazoMultaPadrao() < 0) {
                throw new BusinessException("Prazo de multa padrão não pode ser negativo.");
            }
            perfil.setPrazoMultaPadrao(dto.prazoMultaPadrao());
        }

        if (dto.prazoDevolucaoPadrao() != null) {
            if (dto.prazoDevolucaoPadrao() < 0) {
                throw new BusinessException("Prazo de devolução padrão não pode ser negativo.");
            }
            perfil.setPrazoDevolucaoPadrao(dto.prazoDevolucaoPadrao());
        }

        perfilUsuarioRepository.save(perfil);

        return new PerfilUsuarioResponseDTO(
                perfil.getId(),
                perfil.getNome(),
                perfil.getMultaPadrao(),
                perfil.getPrazoDevolucaoPadrao(),
                perfil.getPrazoMultaPadrao(),
                perfil.getBiblioteca().getId()
        );
    }

    @Transactional
    public void deletarPerfilUsuario(UUID idPerfilUsuario) {
        PerfilUsuario perfilUsuario = perfilUsuarioRepository.findById(idPerfilUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de Usuário não encontrado."));

        boolean hasActiveUsuarioBiblioteca = perfilUsuarioRepository.existsActiveUsuarioBibliotecaByPerfilUsuarioId(idPerfilUsuario);

        if (hasActiveUsuarioBiblioteca) {
            throw new BusinessException("Não é possível excluir o perfil: Existem usuários de biblioteca ativos utilizando este perfil.");
        }

        perfilUsuario.setDeletado(true);
        perfilUsuarioRepository.save(perfilUsuario);
    }
}
