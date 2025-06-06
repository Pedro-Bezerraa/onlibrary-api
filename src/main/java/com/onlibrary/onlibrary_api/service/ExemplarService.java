package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.exemplar.AttExemplarRequestDTO;
import com.onlibrary.onlibrary_api.dto.exemplar.ExemplarRequestDTO;
import com.onlibrary.onlibrary_api.dto.exemplar.ExemplarResponseDTO;
import com.onlibrary.onlibrary_api.exception.BusinessException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.*;
import com.onlibrary.onlibrary_api.model.enums.SituacaoExemplar;
import com.onlibrary.onlibrary_api.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExemplarService {
    private final ReservaExemplarRepository reservaExemplarRepository;
    private final ExemplarRepository exemplarRepository;
    private final LivroRepository livroRepository;
    private final BibliotecaRepository bibliotecaRepository;
    private final ReservaRepository reservaRepository;
    private final BibliotecaLivroRepository bibliotecaLivroRepository;

    @Transactional
    public ExemplarResponseDTO criarExemplar(ExemplarRequestDTO dto) {

        Biblioteca biblioteca = bibliotecaRepository.findById(dto.bibliotecaId())
                .orElseThrow(() -> new ResourceNotFoundException("Biblioteca não encontrada"));

        Livro livro = livroRepository.findById(dto.livroId())
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado"));

        boolean existeBibliotecaLivro = bibliotecaLivroRepository
                .existsByBibliotecaIdAndLivroId(dto.bibliotecaId(), dto.livroId());

        if (!existeBibliotecaLivro) {
            BibliotecaLivro bibliotecaLivro = new BibliotecaLivro();
            bibliotecaLivro.setBiblioteca(biblioteca);
            bibliotecaLivro.setLivro(livro);
            bibliotecaLivroRepository.save(bibliotecaLivro);
        }

        Exemplar exemplar = new Exemplar();
        exemplar.setBiblioteca(biblioteca);
        exemplar.setLivro(livro);
        exemplar.setNumeroTombo(dto.numeroTombo());
        exemplar.setSetor(dto.setor());
        exemplar.setPrateleira(dto.prateleira());
        exemplar.setEstante(dto.estante());
        exemplar.setSituacao(SituacaoExemplar.DISPONIVEL);

        exemplarRepository.save(exemplar);

        return new ExemplarResponseDTO(
                exemplar.getId(),
                exemplar.getNumeroTombo(),
                exemplar.getSetor(),
                exemplar.getPrateleira(),
                exemplar.getEstante(),
                exemplar.getSituacao(),
                exemplar.getLivro().getId(),
                exemplar.getBiblioteca().getId()
        );
    }

    @Transactional
    public ExemplarResponseDTO atualizarExemplar(UUID id, AttExemplarRequestDTO dto) {
        Exemplar exemplar = exemplarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exemplar não encontrado."));

        if (exemplar.getSituacao() != SituacaoExemplar.DISPONIVEL) {
            throw new BusinessException("Exemplar não está disponível para alteração.");
        }

        Livro livro = livroRepository.findById(dto.livroId())
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado."));

        exemplar.setLivro(livro);
        exemplar.setNumeroTombo(dto.numeroTombo());
        exemplar.setEstante(dto.estante());
        exemplar.setPrateleira(dto.prateleira());
        exemplar.setSetor(dto.setor());
        exemplar.setSituacao(dto.situacao());

        exemplarRepository.save(exemplar);

        reservaRepository.findFirstBySituacaoAndLivroOrderByDataEmissaoAsc("PENDENTE", livro)
                .ifPresent(reserva -> {
                    ReservaExemplar reservaExemplar = ReservaExemplar.builder()
                            .exemplar(exemplar)
                            .reserva(reserva)
                            .build();
                    reservaExemplarRepository.save(reservaExemplar);

                    if (reserva.getQuantidadePendente().compareTo(BigDecimal.ZERO) > 0) {
                        reserva.setQuantidadePendente(reserva.getQuantidadePendente().subtract(BigDecimal.ONE));
                        reservaRepository.save(reserva);
                    }
                });

        return new ExemplarResponseDTO(
                exemplar.getId(),
                exemplar.getNumeroTombo(),
                exemplar.getSetor(),
                exemplar.getPrateleira(),
                exemplar.getEstante(),
                exemplar.getSituacao(),
                exemplar.getLivro().getId(),
                exemplar.getBiblioteca().getId()
        );
    }
}
