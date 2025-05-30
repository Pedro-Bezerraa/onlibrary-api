package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.AttPerfilUsuarioRequestDTO;
import com.onlibrary.onlibrary_api.dto.PerfilUsuarioRequestDTO;
import com.onlibrary.onlibrary_api.exception.InvalidCredentialsException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.PerfilUsuario;
import com.onlibrary.onlibrary_api.repository.BibliotecaRepository;
import com.onlibrary.onlibrary_api.repository.PerfilUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PerfilService {
    private final PerfilUsuarioRepository perfilUsuarioRepository;
    private final BibliotecaRepository bibliotecaRepository;

    public void criarPerfil(PerfilUsuarioRequestDTO dto) {

        if (dto.multaPadrao() < 0 || dto.prazoMultaPadrao() < 0 || dto.prazoDevolucaoPadrao() < 0) {
            throw new InvalidCredentialsException("Valores negativos não são válidos");
        }

        boolean nomeExiste = perfilUsuarioRepository.existsByNome(dto.nome());
        if (nomeExiste) {
            throw new InvalidCredentialsException("Já existe um perfil com esse nome");
        }

        PerfilUsuario perfil = new PerfilUsuario();

        perfil.setBiblioteca(bibliotecaRepository.findById(dto.bibliotecaId())
                .orElseThrow(() -> new ResourceNotFoundException(("Biblioteca não encontrada"))));
        perfil.setNome(dto.nome());
        perfil.setMultaPadrao(dto.multaPadrao());
        perfil.setPrazoDevolucaoPadrao(dto.prazoDevolucaoPadrao());
        perfil.setPrazoMultaPadrao(dto.prazoMultaPadrao());

        perfilUsuarioRepository.save(perfil);
    }

    public void atualizarPerfil(AttPerfilUsuarioRequestDTO dto, UUID id) {
        PerfilUsuario perfilNovo = perfilUsuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado"));

        if (dto.multaPadrao() < 0 || dto.prazoMultaPadrao() < 0 || dto.prazoDevolucaoPadrao() < 0) {
            throw new InvalidCredentialsException("Valores negativos não são válidos");
        }

        boolean nomeExiste = perfilUsuarioRepository.existsByNome(dto.nome());
        if (nomeExiste) {
            throw new InvalidCredentialsException("Já existe um perfil com esse nome");
        }

        perfilNovo.setNome(dto.nome());
        perfilNovo.setMultaPadrao(dto.multaPadrao());
        perfilNovo.setPrazoMultaPadrao(dto.prazoMultaPadrao());
        perfilNovo.setPrazoDevolucaoPadrao(dto.prazoDevolucaoPadrao());

        perfilUsuarioRepository.save(perfilNovo);
    }
}
