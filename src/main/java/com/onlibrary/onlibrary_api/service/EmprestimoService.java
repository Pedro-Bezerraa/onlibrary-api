package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.emprestimo.AttEmprestimoRequestDTO;
import com.onlibrary.onlibrary_api.dto.emprestimo.EmprestimoRequestDTO;
import com.onlibrary.onlibrary_api.dto.emprestimo.EmprestimoResponseDTO;
import com.onlibrary.onlibrary_api.exception.BusinessException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.*;
import com.onlibrary.onlibrary_api.model.enums.ContaSituacao;
import com.onlibrary.onlibrary_api.model.enums.SituacaoEmprestimo;
import com.onlibrary.onlibrary_api.model.enums.SituacaoExemplar;
import com.onlibrary.onlibrary_api.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmprestimoService {
    private final EmprestimoRepository emprestimoRepository;
    private final ExemplarRepository exemplarRepository;
    private final ReservaRepository reservaRepository;
    private final ReservaExemplarRepository reservaExemplarRepository;
    private final UsuarioBibliotecaRepository usuarioBibliotecaRepository;
    private final UsuarioRepository usuarioRepository;
    private final BibliotecaRepository bibliotecaRepository;
    private final EmprestimoExemplarRepository emprestimoExemplarRepository;

    @Transactional
    public EmprestimoResponseDTO criarEmprestimo(EmprestimoRequestDTO dto) {
        UsuarioBiblioteca usuarioBiblioteca = usuarioBibliotecaRepository.findById(dto.usuarioBibliotecaId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário da biblioteca não encontrado!"));

        if (usuarioBiblioteca.getSituacao() == ContaSituacao.BLOQUEADO) {
            throw new BusinessException("Usuário está bloqueado!");
        }

        Usuario bibliotecario = usuarioRepository.findById(dto.bibliotecarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Bibliotecário não encontrado!"));

        if (bibliotecario.getSituacao() == ContaSituacao.BLOQUEADO) {
            throw new BusinessException("Bibliotecário está bloqueado!");
        }

        Biblioteca biblioteca = bibliotecaRepository.findById(dto.bibliotecaId())
                .orElseThrow(() -> new ResourceNotFoundException("Biblioteca não encontrada!"));

        List<Exemplar> exemplares = exemplarRepository.findAllById(dto.exemplares());

        if (exemplares.size() != dto.exemplares().size()) {
            throw new BusinessException("Um ou mais exemplares não foram encontrados.");
        }

        for (Exemplar exemplar : exemplares) {
            if (!exemplar.getBiblioteca().getId().equals(biblioteca.getId())) {
                throw new BusinessException("O exemplar " + exemplar.getId() + " não pertence à biblioteca.");
            }
            if (exemplar.getSituacao() == SituacaoExemplar.INDISPONIVEL) {
                throw new BusinessException("O exemplar " + exemplar.getId() + " está indisponível.");
            }
        }

        exemplares.forEach(exemplar -> exemplar.setSituacao(SituacaoExemplar.INDISPONIVEL));
        exemplarRepository.saveAll(exemplares);

        Emprestimo emprestimo = Emprestimo.builder()
                .biblioteca(biblioteca)
                .usuarioBiblioteca(usuarioBiblioteca)
                .bibliotecario(bibliotecario)
                .situacao(SituacaoEmprestimo.PENDENTE)
                .dataEmissao(LocalDate.now())
                .dataDevolucao(LocalDate.now().plusDays(usuarioBiblioteca.getPerfilUsuario().getPrazoDevolucaoPadrao()))
                .build();

        emprestimoRepository.save(emprestimo);

        List<EmprestimoExemplar> emprestimoExemplares = exemplares.stream().map(exemplar ->
                EmprestimoExemplar.builder()
                        .emprestimo(emprestimo)
                        .exemplar(exemplar)
                        .build()
        ).toList();

        emprestimoExemplarRepository.saveAll(emprestimoExemplares);

        return new EmprestimoResponseDTO(
                emprestimo.getId(),
                emprestimo.getDataEmissao(),
                emprestimo.getDataDevolucao(),
                emprestimo.getSituacao(),
                usuarioBiblioteca.getId(),
                bibliotecario.getId(),
                biblioteca.getId(),
                exemplares.stream().map(Exemplar::getId).toList()
        );
    }


    @Transactional
    public EmprestimoResponseDTO atualizarEmprestimo(UUID id, AttEmprestimoRequestDTO dto) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empréstimo não encontrado!"));

        boolean estaFinalizado = emprestimo.getSituacao() == SituacaoEmprestimo.CONCLUIDO
                || emprestimo.getSituacao() == SituacaoEmprestimo.CANCELADO;

        if (estaFinalizado) {
            throw new BusinessException("Não é possível atualizar um empréstimo já finalizado.");
        }

        if (dto.dataDevolucao() != null) {
            emprestimo.setDataDevolucao(dto.dataDevolucao());
        }

        emprestimo.setSituacao(dto.situacao());
        emprestimoRepository.save(emprestimo);

        if (dto.situacao() == SituacaoEmprestimo.CONCLUIDO || dto.situacao() == SituacaoEmprestimo.CANCELADO) {
            liberarExemplares(emprestimo);
        }

        return new EmprestimoResponseDTO(
                emprestimo.getId(),
                emprestimo.getDataEmissao(),
                emprestimo.getDataDevolucao(),
                emprestimo.getSituacao(),
                emprestimo.getUsuarioBiblioteca().getId(),
                emprestimo.getBibliotecario().getId(),
                emprestimo.getBiblioteca().getId(),
                emprestimo.getExemplares().stream().map(e -> e.getExemplar().getId()).toList()
        );
    }

    private void liberarExemplares(Emprestimo emprestimo) {
        List<EmprestimoExemplar> emprestimoExemplares = emprestimo.getExemplares();

        List<Exemplar> exemplaresAtualizados = new ArrayList<>();

        for (EmprestimoExemplar ee : emprestimoExemplares) {
            Exemplar exemplar = ee.getExemplar();
            exemplar.setSituacao(SituacaoExemplar.DISPONIVEL);
            exemplaresAtualizados.add(exemplar);

            List<Reserva> reservasPendentes = reservaRepository
                    .findReservasPendentesPorLivro(exemplar.getLivro().getId());

            for (Reserva reserva : reservasPendentes) {
                BigDecimal qntPendente = reserva.getQuantidadePendente();

                if (qntPendente.compareTo(BigDecimal.ZERO) > 0) {
                    ReservaExemplar reservaExemplar = ReservaExemplar.builder()
                            .exemplar(exemplar)
                            .reserva(reserva)
                            .build();

                    reservaExemplarRepository.save(reservaExemplar);

                    reserva.setQuantidadePendente(qntPendente.subtract(BigDecimal.ONE));
                    reservaRepository.save(reserva);

                    break;
                }
            }
        }

        exemplarRepository.saveAll(exemplaresAtualizados);
    }
}
