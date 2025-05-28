package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.biblioteca.EmprestimoRequestDTO;
import com.onlibrary.onlibrary_api.model.entities.*;
import com.onlibrary.onlibrary_api.model.enums.SituacaoEmprestimo;
import com.onlibrary.onlibrary_api.repository.EmprestimoRepository;
import com.onlibrary.onlibrary_api.repository.ExemplarRepository;
import com.onlibrary.onlibrary_api.repository.ReservaExemplarRepository;
import com.onlibrary.onlibrary_api.repository.ReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmprestimoService {
    private final EmprestimoRepository emprestimoRepository;
    private final ExemplarRepository exemplarRepository;
    private final ReservaRepository reservaRepository;
    private final ReservaExemplarRepository reservaExemplarRepository;

    public void atualizarEmprestimo(UUID id, EmprestimoRequestDTO dto) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));

        SituacaoEmprestimo novaSituacao;
        try {
            novaSituacao = SituacaoEmprestimo.valueOf(dto.situacao().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Situação inválida: " + dto.situacao());
        }

        boolean isFinalizado = emprestimo.getSituacao() == SituacaoEmprestimo.CONCLUIDO
                || emprestimo.getSituacao() == SituacaoEmprestimo.CANCELADO;

        if (isFinalizado && !emprestimo.getDataDevolucao().equals(dto.dataDevolucao())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Não é permitido alterar a data de devolução de um empréstimo finalizado ou cancelado.");
        }

        emprestimo.setSituacao(novaSituacao);

        if (!isFinalizado) {
            emprestimo.setDataDevolucao(dto.dataDevolucao());
        }

        emprestimoRepository.save(emprestimo);

        if (novaSituacao == SituacaoEmprestimo.CONCLUIDO || novaSituacao == SituacaoEmprestimo.CANCELADO) {
            for (EmprestimoExemplar ee : emprestimo.getExemplares()) {
                Exemplar exemplar = ee.getExemplar();

                exemplar.setDisponivel(true);
                exemplarRepository.save(exemplar);

                List<Reserva> reservasPendentes = reservaRepository
                        .findReservasPendentesByLivro(exemplar.getLivro(), "PENDENTE");

                for (Reserva reserva : reservasPendentes) {
                    if (reserva.getQuantidadePendente().compareTo(BigDecimal.ZERO) > 0) {
                        ReservaExemplar reservaExemplar = ReservaExemplar.builder()
                                .reserva(reserva)
                                .exemplar(exemplar)
                                .build();
                        reservaExemplarRepository.save(reservaExemplar);

                        reserva.setQuantidadePendente(reserva.getQuantidadePendente().subtract(BigDecimal.ONE));
                        reservaRepository.save(reserva);
                        break;
                    }
                }
            }
        }
    }
}
