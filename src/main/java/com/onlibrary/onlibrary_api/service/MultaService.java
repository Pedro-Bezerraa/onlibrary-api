package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.multa.AttMultaRequestDTO;
import com.onlibrary.onlibrary_api.dto.multa.AttMultaResponseDTO;
import com.onlibrary.onlibrary_api.dto.multa.MultaRequestDTO;
import com.onlibrary.onlibrary_api.dto.multa.MultaResponseDTO;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.*;
import com.onlibrary.onlibrary_api.model.enums.SituacaoMulta;
import com.onlibrary.onlibrary_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MultaService {
    private final MultaRepository multaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PerfilUsuarioRepository perfilUsuarioRepository;
    private final UsuarioBibliotecaRepository usuarioBibliotecaRepository;
    private final BibliotecaRepository bibliotecaRepository;

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

    public AttMultaResponseDTO atualizarMulta(UUID multaId, AttMultaRequestDTO dto) {
        Multa multa = multaRepository.findById(multaId)
                .orElseThrow(() -> new ResourceNotFoundException("Multa não encontrada."));

        multa.setMotivo(dto.motivo());

        multaRepository.save(multa);

        return new AttMultaResponseDTO(multa.getId(), multa.getUsuario().getId(), multa.getBibliotecario().getId(), multa.getValor(), multa.getDataEmissao(), multa.getDataVencimento(), multa.getSituacao(), multa.getMotivo(), multa.getBiblioteca().getId(), null);
    }
}
