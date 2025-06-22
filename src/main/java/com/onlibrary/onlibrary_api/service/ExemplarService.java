package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.exemplar.UpdateExemplarRequestDTO;
import com.onlibrary.onlibrary_api.dto.exemplar.ExemplarRequestDTO;
import com.onlibrary.onlibrary_api.dto.exemplar.ExemplarResponseDTO;
import com.onlibrary.onlibrary_api.exception.BusinessException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.*;
import com.onlibrary.onlibrary_api.model.enums.SituacaoEmprestimo;
import com.onlibrary.onlibrary_api.model.enums.SituacaoExemplar;
import com.onlibrary.onlibrary_api.model.enums.SituacaoReserva;
import com.onlibrary.onlibrary_api.model.enums.TipoUsuario;
import com.onlibrary.onlibrary_api.repository.entities.*;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
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
    private final NotificacaoService notificacaoService;
    private final EmprestimoRepository emprestimoRepository;

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

        atenderReservasPendentes(exemplar);

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
    public ExemplarResponseDTO atualizarExemplar(UUID id, UpdateExemplarRequestDTO dto) {
        Exemplar exemplar = exemplarRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exemplar não encontrado."));

        if (exemplar.getSituacao() == SituacaoExemplar.RESERVADO || exemplar.getSituacao() == SituacaoExemplar.EMPRESTADO) {
            throw new BusinessException("Exemplar está sendo usado no momento, não disponível para alteração.");
        }

        if (dto.livroId() != null) {
            Livro livro = livroRepository.findById(dto.livroId())
                    .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado."));
            exemplar.setLivro(livro);
        }

        if (dto.numeroTombo() != null) exemplar.setNumeroTombo(dto.numeroTombo());
        if (dto.estante() != null) exemplar.setEstante(dto.estante());
        if (dto.prateleira() != null) exemplar.setPrateleira(dto.prateleira());
        if (dto.setor() != null) exemplar.setSetor(dto.setor());

        if (dto.situacao() != null) {
            exemplar.setSituacao(dto.situacao());

            if (dto.situacao() == SituacaoExemplar.DISPONIVEL) {
                exemplarRepository.save(exemplar);
                atenderReservasPendentes(exemplar);
            }
        }

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
    public void deletarExemplar(UUID idExemplar) {
        Exemplar exemplar = exemplarRepository.findById(idExemplar)
                .orElseThrow(() -> new ResourceNotFoundException("Exemplar não encontrado."));

        boolean hasActiveReserva = reservaExemplarRepository.existsActiveReservasByExemplarId(idExemplar);

        if (hasActiveReserva) {
            throw new BusinessException("Não é possível excluir o exemplar: Existem reservas ativas associadas a ele.");
        }

        boolean hasActiveEmprestimo = emprestimoRepository.existsByExemplarAndSituacao(exemplar, SituacaoEmprestimo.PENDENTE);
        if (hasActiveEmprestimo) {
            throw new BusinessException("Não é possível excluir o exemplar: Existem empréstimos pendentes associados a ele.");
        }

        exemplar.setDeletado(true);
        exemplarRepository.save(exemplar);
    }

    private void atenderReservasPendentes(Exemplar exemplar) {
        List<Reserva> reservasPendentes = reservaRepository.findReservasPorLivroEBibliotecaComSituacao(
                exemplar.getLivro().getId(),
                exemplar.getBiblioteca().getId(),
                SituacaoReserva.PENDENTE
        );

        for (Reserva reserva : reservasPendentes) {
            BigDecimal qntPendente = reserva.getQuantidadePendente();
            if (qntPendente != null && qntPendente.compareTo(BigDecimal.ZERO) > 0) {
                ReservaExemplar reservaExemplar = ReservaExemplar.builder()
                        .exemplar(exemplar)
                        .reserva(reserva)
                        .build();
                reservaExemplarRepository.save(reservaExemplar);

                reserva.setQuantidadePendente(qntPendente.subtract(BigDecimal.ONE));

                if (reserva.getQuantidadePendente().compareTo(BigDecimal.ZERO) <= 0) {
                    reserva.setSituacao(SituacaoReserva.ATENDIDO_PARCIALMENTE);

                    notificacaoService.notificarUsuario(
                            reserva.getUsuario(),
                            "Reserva pronta para retirada",
                            "Sua reserva do livro '" + reserva.getLivro().getTitulo() +
                                    "' na biblioteca '" + reserva.getBiblioteca().getNome() +
                                    "' está disponível para retirada.",
                            TipoUsuario.COMUM
                    );
                }

                exemplar.setSituacao(SituacaoExemplar.RESERVADO);

                reservaRepository.save(reserva);
                exemplarRepository.save(exemplar);
                break;
            }
        }
    }
}
