package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.biblioteca.BibliotecaDTO;
import com.onlibrary.onlibrary_api.dto.biblioteca.CreateBibliotecaDTO;
import com.onlibrary.onlibrary_api.dto.biblioteca.EnderecoDTO;
import com.onlibrary.onlibrary_api.model.*;
import com.onlibrary.onlibrary_api.repository.BibliotecaRepository;
import com.onlibrary.onlibrary_api.repository.PerfilUsuarioBibliotecaRepository;
import com.onlibrary.onlibrary_api.repository.UsuarioBibliotecaRepository;
import com.onlibrary.onlibrary_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BibliotecaService {
    private final BibliotecaRepository bibliotecaRepository;
    private final PerfilUsuarioBibliotecaRepository perfilRepository;
    private final UsuarioBibliotecaRepository usuarioBibliotecaRepository;
    private final UsuarioRepository usuarioRepository;

    public Long criarBiblioteca(CreateBibliotecaDTO dto, Long idUsuarioCriador) {

        Endereco endereco = new Endereco();
        endereco.setRua(dto.getEndereco().getRua());
        endereco.setNumero(dto.getEndereco().getNumero());
        endereco.setCep(dto.getEndereco().getCep());


        Biblioteca biblioteca = new Biblioteca();
        biblioteca.setNome(dto.getNome());
        biblioteca.setEndereco(endereco);
        biblioteca.setTelefone(dto.getTelefone());
        biblioteca.setAplicacaoMulta(dto.getAplicacaoMulta());
        biblioteca.setReservarOnline(dto.getReservaOnline());

        biblioteca = bibliotecaRepository.save(biblioteca);

        PerfilUsuarioBiblioteca perfil = new PerfilUsuarioBiblioteca();
        perfil.setNome("BIBLIOTECARIO");
        perfil.setMultaPadrao(BigDecimal.ZERO);
        perfil.setDataDevolucaoPadrao(30);
        perfil.setBiblioteca(biblioteca);

        perfil = perfilRepository.save(perfil);

        Usuario usuario = usuarioRepository.findById(idUsuarioCriador)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        UsuarioBiblioteca usuarioBiblioteca = new UsuarioBiblioteca();
        usuarioBiblioteca.setBiblioteca(biblioteca);
        usuarioBiblioteca.setUsuario(usuario);
        usuarioBiblioteca.setPerfilUsuarioBiblioteca(perfil);
        usuarioBiblioteca.setTipoUsuario(String.valueOf(TipoUsuario.ADMIN_MASTER));
        usuarioBiblioteca.setNumeroMatricula(gerarNumeroMatricula());

        usuarioBibliotecaRepository.save(usuarioBiblioteca);

        return biblioteca.getId();
    }

    public BibliotecaDTO toDTO(Biblioteca biblioteca) {
        Endereco endereco = biblioteca.getEndereco();
        EnderecoDTO enderecoDTO = null;
        if (endereco != null) {
            enderecoDTO = new EnderecoDTO(endereco.getRua(), endereco.getNumero(), endereco.getCep());
        }

        return new BibliotecaDTO(
                biblioteca.getId(),
                biblioteca.getTelefone(),
                biblioteca.getAplicacaoMulta(),
                biblioteca.getReservarOnline(),
                enderecoDTO
        );
    }
    public List<BibliotecaDTO> buscarBibliotecasDoUsuarioDTO(Long usuarioId) {
        List<Biblioteca> bibliotecas = usuarioBibliotecaRepository.findBibliotecasDoUsuarioAdministradorOuFuncionario(usuarioId);
        return bibliotecas.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private String gerarNumeroMatricula() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
