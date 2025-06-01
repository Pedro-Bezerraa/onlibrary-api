package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.usuarioBiblioteca.AttUsuarioBibliotecaRequestDTO;
import com.onlibrary.onlibrary_api.dto.usuarioBiblioteca.UsuarioBibliotecaRequestDTO;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioBibliotecaService {
    private final UsuarioBibliotecaRepository usuarioBibliotecaRepository;
    private final BibliotecaRepository bibliotecaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilUsuarioRepository perfilUsuarioRepository;

    public void criarUsuarioBiblioteca(UsuarioBibliotecaRequestDTO dto) {
        Biblioteca biblioteca = bibliotecaRepository.findById(dto.bibliotecaId())
                .orElseThrow(() -> new ResourceNotFoundException("Biblioteca não encontrada"));

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        PerfilUsuario perfilUsuario = perfilUsuarioRepository.findById(dto.perfilUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado"));

        TipoUsuario tipoUsuario = TipoUsuario.fromString(dto.tipoUsuario());

        if (!usuario.getCpf().equals(dto.cpf())) {
            throw new IllegalArgumentException("O CPF informado não corresponde ao CPF do usuário.");
        }

        String nomePerfil = perfilUsuario.getNome().toLowerCase();

        if (tipoUsuario == TipoUsuario.COMUM && nomePerfil.equals("bibliotecario")) {
            throw new IllegalArgumentException("Usuários do tipo COMUM não podem ter o perfil de BIBLIOTECARIO.");
        }

        if (tipoUsuario == TipoUsuario.ADMIN && !nomePerfil.equals("bibliotecario")) {
            throw new IllegalArgumentException("Usuários do tipo ADMIN não podem ter o perfil de OUTRO.");
        }

        if (nomePerfil.equals("bibliotecario") && tipoUsuario != TipoUsuario.ADMIN) {
            throw new IllegalArgumentException("Perfis de BIBLIOTECARIO devem ser do tipo ADMIN.");
        }

        if (nomePerfil.equals("outro") && tipoUsuario != TipoUsuario.COMUM) {
            throw new IllegalArgumentException("Perfis de OUTRO devem ser do tipo COMUM.");
        }

        UsuarioBiblioteca usuarioBiblioteca = new UsuarioBiblioteca();
        usuarioBiblioteca.setBiblioteca(biblioteca);
        usuarioBiblioteca.setUsuario(usuario);
        usuarioBiblioteca.setPerfilUsuario(perfilUsuario);
        usuarioBiblioteca.setTipoUsuario(tipoUsuario);
        usuarioBiblioteca.setNumeroMatricula(dto.numeroMatricula());
        usuarioBiblioteca.setCpf(dto.cpf());
        usuarioBiblioteca.setSituacao(ContaSituacao.ATIVO);

        usuarioBibliotecaRepository.save(usuarioBiblioteca);
    }

    public void atualizarUsuarioBiblioteca(AttUsuarioBibliotecaRequestDTO dto, UUID id) {
        UsuarioBiblioteca usuarioBiblioteca = usuarioBibliotecaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UsuárioBiblioteca não encontrado"));

        PerfilUsuario perfilUsuario = perfilUsuarioRepository.findById(dto.perfilUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado"));

        String nomePerfil = perfilUsuario.getNome().toLowerCase();
        TipoUsuario tipoUsuario = TipoUsuario.fromString(dto.tipoUsuario());

        if (tipoUsuario == TipoUsuario.COMUM && nomePerfil.equals("bibliotecario")) {
            throw new IllegalArgumentException("Usuários do tipo COMUM não podem ter o perfil de BIBLIOTECARIO.");
        }

        if (tipoUsuario == TipoUsuario.ADMIN && nomePerfil.equals("outro")) {
            throw new IllegalArgumentException("Usuários do tipo ADMIN não podem ter o perfil de OUTRO.");
        }

        if (nomePerfil.equals("bibliotecario") && tipoUsuario != TipoUsuario.ADMIN) {
            throw new IllegalArgumentException("Perfis de BIBLIOTECARIO devem ser do tipo ADMIN.");
        }

        if (nomePerfil.equals("outro") && tipoUsuario != TipoUsuario.COMUM) {
            throw new IllegalArgumentException("Perfis de OUTRO devem ser do tipo COMUM.");
        }

        usuarioBiblioteca.setPerfilUsuario(perfilUsuario);
        usuarioBiblioteca.setTipoUsuario(tipoUsuario);
        usuarioBiblioteca.setNumeroMatricula(dto.numeroMatricula());

        usuarioBibliotecaRepository.save(usuarioBiblioteca);
    }
}
