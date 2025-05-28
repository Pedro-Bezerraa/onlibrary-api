package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.biblioteca.ExemplarRequestDTO;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.Exemplar;
import com.onlibrary.onlibrary_api.model.entities.Reserva;
import com.onlibrary.onlibrary_api.model.entities.ReservaExemplar;
import com.onlibrary.onlibrary_api.model.enums.SituacaoEmprestimo;
import com.onlibrary.onlibrary_api.model.enums.SituacaoExemplar;
import com.onlibrary.onlibrary_api.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExemplarService {
    private final EmprestimoRepository emprestimoRepository;
    private final ReservaExemplarRepository reservaExemplarRepository;
    private final ExemplarRepository exemplarRepository;
    private final LivroRepository livroRepository;
    private final BibliotecaRepository bibliotecaRepository;
    private final ReservaRepository reservaRepository;

    @Transactional
    public void atualizarExemlar(UUID id, ExemplarRequestDTO dto) {
        Exemplar exemplar = exemplarRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Exemplar não encontrado."));

        if (exemplar.getSituacao() != SituacaoExemplar.DISPONIVEL) {
            throw new IllegalStateException("Exemplar não está disponível para alteração.");
        } else {
            exemplar.setLivro(livroRepository.findById(dto.livroId())
                    .orElseThrow(() -> new ResourceNotFoundException(("Livro não encontrado"))));
            exemplar.setNumeroTombo(dto.numeroTombo());
            exemplar.setEstante(dto.estante());
            exemplar.setPrateleira(dto.prateleira());
            exemplar.setSetor(dto.setor());
            exemplar.setBiblioteca(bibliotecaRepository.findById(dto.bibliotecaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Biblioteca não encontrada")));

            exemplarRepository.save(exemplar);
        }

        Optional<Reserva> reservaOpt = reservaRepository.findFirstBySituacaoAndLivroOrderByDataEmissaoAsc(
                "PENDENTE", exemplar.getLivro());

        if (reservaOpt.isPresent()) {
            Reserva reserva = reservaOpt.get();

            ReservaExemplar reservaExemplar = ReservaExemplar.builder()
                    .exemplar(exemplar)
                    .reserva(reserva)
                    .build();

            reservaExemplarRepository.save(reservaExemplar);
        }
    }

    private boolean exemplarDisponivel(Exemplar exemplar) {
        boolean temEmprestimos = emprestimoRepository.existsByExemplarAndSituacao(
                exemplar, SituacaoEmprestimo.CONCLUIDO);

        boolean temReservas = reservaExemplarRepository.existsByExemplar(exemplar);

        return !temEmprestimos && !temReservas;
    }
}
