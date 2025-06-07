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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioBibliotecaService {
    private final UsuarioBibliotecaRepository usuarioBibliotecaRepository;
    private final BibliotecaRepository bibliotecaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilUsuarioRepository perfilUsuarioRepository;

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

        PerfilUsuario perfilUsuario = perfilUsuarioRepository.findById(dto.perfilUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado."));

        String nomePerfil = perfilUsuario.getNome().toLowerCase();

        if (dto.tipoUsuario() == TipoUsuario.COMUM && nomePerfil.equalsIgnoreCase("bibliotecario")) {
            throw new BusinessException("Usuários do tipo COMUM não podem ter o perfil de BIBLIOTECARIO.");
        }

        if (dto.tipoUsuario() == TipoUsuario.ADMIN && nomePerfil.equalsIgnoreCase("outro")) {
            throw new BusinessException("Usuários do tipo ADMIN não podem ter o perfil de OUTRO.");
        }

        if (nomePerfil.equalsIgnoreCase("bibliotecario") && dto.tipoUsuario() != TipoUsuario.ADMIN) {
            throw new BusinessException("Perfis de BIBLIOTECARIO devem ser do tipo ADMIN.");
        }


        usuarioBiblioteca.setPerfilUsuario(perfilUsuario);
        usuarioBiblioteca.setTipoUsuario(dto.tipoUsuario());
        usuarioBiblioteca.setNumeroMatricula(dto.numeroMatricula());

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
}
