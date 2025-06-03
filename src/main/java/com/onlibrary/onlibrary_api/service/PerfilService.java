package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.perfilUsuario.AttPerfilUsuarioRequestDTO;
import com.onlibrary.onlibrary_api.dto.perfilUsuario.PerfilUsuarioRequestDTO;
import com.onlibrary.onlibrary_api.dto.perfilUsuario.PerfilUsuarioResponseDTO;
import com.onlibrary.onlibrary_api.exception.BusinessException;
import com.onlibrary.onlibrary_api.exception.ConflictException;
import com.onlibrary.onlibrary_api.exception.InvalidCredentialsException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.Biblioteca;
import com.onlibrary.onlibrary_api.model.entities.PerfilUsuario;
import com.onlibrary.onlibrary_api.repository.BibliotecaRepository;
import com.onlibrary.onlibrary_api.repository.PerfilUsuarioRepository;
import jakarta.transaction.Transactional;
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
    public PerfilUsuarioResponseDTO atualizarPerfil(AttPerfilUsuarioRequestDTO dto, UUID id) {
        PerfilUsuario perfil = perfilUsuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado."));

        if (dto.multaPadrao() < 0) {
            throw new BusinessException("Multa padrão não pode ser negativa.");
        }
        if (dto.prazoMultaPadrao() < 0) {
            throw new BusinessException("Prazo de multa padrão não pode ser negativo.");
        }
        if (dto.prazoDevolucaoPadrao() < 0) {
            throw new BusinessException("Prazo de devolução padrão não pode ser negativo.");
        }

        boolean nomeExiste = perfilUsuarioRepository.existsByNomeAndBibliotecaIdAndIdNot(
                dto.nome(), perfil.getBiblioteca().getId(), id);

        if (nomeExiste) {
            throw new ConflictException("Já existe um perfil com esse nome nessa biblioteca.");
        }

        perfil.setNome(dto.nome());
        perfil.setMultaPadrao(dto.multaPadrao());
        perfil.setPrazoMultaPadrao(dto.prazoMultaPadrao());
        perfil.setPrazoDevolucaoPadrao(dto.prazoDevolucaoPadrao());

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
}
