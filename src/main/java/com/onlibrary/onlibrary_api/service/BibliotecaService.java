package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.biblioteca.BibliotecaRequestDTO;
import com.onlibrary.onlibrary_api.dto.biblioteca.BibliotecaResponseDTO;
import com.onlibrary.onlibrary_api.dto.biblioteca.ContagemResponseDTO;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.Biblioteca;
import com.onlibrary.onlibrary_api.model.entities.PerfilUsuario;
import com.onlibrary.onlibrary_api.model.entities.Usuario;
import com.onlibrary.onlibrary_api.model.entities.UsuarioBiblioteca;
import com.onlibrary.onlibrary_api.model.enums.ContaSituacao;
import com.onlibrary.onlibrary_api.model.enums.TipoUsuario;
import com.onlibrary.onlibrary_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BibliotecaService {
    private final JwtService jwtService;
    private final BibliotecaRepository bibliotecaRepository;
    private final PerfilUsuarioRepository perfilUsuarioRepository;
    private final UsuarioBibliotecaRepository usuarioBibliotecaRepository;
    private final UsuarioRepository usuarioRepository;
    private final BibliotecaLivroRepository bibliotecaLivroRepository;

    public UUID criarBiblioteca(BibliotecaRequestDTO dto, UUID idUsuarioCriador) {
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.setNome(dto.nome());
        biblioteca.setTelefone(dto.telefone());
        biblioteca.setRua(dto.rua());
        biblioteca.setNumero(dto.numero());
        biblioteca.setCep(dto.cep());
        biblioteca.setAplicacaoMulta(dto.aplicacaoMulta());
        biblioteca.setReservaOnline(dto.reservaOnline());
        biblioteca.setAplicacaoBloqueio(dto.aplicacaoBloqueio());

        biblioteca = bibliotecaRepository.save(biblioteca);

        PerfilUsuario perfil = new PerfilUsuario();
        perfil.setNome("Bibliotecario");
        perfil.setMultaPadrao(0);
        perfil.setPrazoDevolucaoPadrao(0);
        perfil.setPrazoMultaPadrao(0);
        perfil.setBiblioteca(biblioteca);

        perfil = perfilUsuarioRepository.save(perfil);

        Usuario usuario = usuarioRepository.findById(idUsuarioCriador)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        UsuarioBiblioteca usuarioBiblioteca = new UsuarioBiblioteca();
        usuarioBiblioteca.setBiblioteca(biblioteca);
        usuarioBiblioteca.setUsuario(usuario);
        usuarioBiblioteca.setPerfilUsuario(perfil);
        usuarioBiblioteca.setTipoUsuario(TipoUsuario.ADMIN_MASTER);
        usuarioBiblioteca.setNumeroMatricula("");
        usuarioBiblioteca.setCpf(usuario.getCpf());
        usuarioBiblioteca.setSituacao(ContaSituacao.ATIVO);

        usuarioBibliotecaRepository.save(usuarioBiblioteca);

        return biblioteca.getId();
    }


    public List<BibliotecaResponseDTO> listarBibliotecasAdminOuFuncionario(String token) {
        UUID usuarioId = jwtService.extractIdForUser(token);

        List<TipoUsuario> tiposPermitidos = List.of(TipoUsuario.ADMIN, TipoUsuario.ADMIN_MASTER);

        List<UsuarioBiblioteca> vinculos = usuarioBibliotecaRepository
                .findByUsuarioIdAndTipoUsuarioIn(usuarioId, tiposPermitidos);

        return vinculos.stream()
                .map(ub -> new BibliotecaResponseDTO(
                        ub.getBiblioteca().getId(),
                        ub.getBiblioteca().getNome(),
                        ub.getBiblioteca().getTelefone(),
                        ub.getBiblioteca().getRua(),
                        ub.getBiblioteca().getNumero(),
                        ub.getBiblioteca().getCep(),
                        ub.getBiblioteca().getAplicacaoMulta(),
                        ub.getBiblioteca().getReservaOnline(),
                        ub.getBiblioteca().getAplicacaoBloqueio()
                ))
                .distinct()
                .collect(Collectors.toList());
    }

    public ContagemResponseDTO contarPorBiblioteca(String type, UUID bibliotecaId) {
        long quantidade = 0;
        boolean aviso = false;

        switch (type.toLowerCase()) {
            case "livro":
                quantidade = bibliotecaLivroRepository.countByBibliotecaId(bibliotecaId);
                break;

            default:
                throw new IllegalArgumentException("Tipo inválido: " + type);
        }
        return new ContagemResponseDTO(quantidade, aviso);
    }
}
