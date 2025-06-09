package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.emprestimo.AttEmprestimoRequestDTO;
import com.onlibrary.onlibrary_api.dto.emprestimo.EmprestimoRequestDTO;
import com.onlibrary.onlibrary_api.dto.emprestimo.EmprestimoResponseDTO;
import com.onlibrary.onlibrary_api.exception.BusinessException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.*;
import com.onlibrary.onlibrary_api.model.enums.*;
import com.onlibrary.onlibrary_api.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EmprestimoService {
    private final NotificacaoService notificacaoService;
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
            if (exemplar.getSituacao() == SituacaoExemplar.INDISPONIVEL || exemplar.getSituacao() == SituacaoExemplar.EMPRESTADO) {
                throw new BusinessException("O exemplar " + exemplar.getId() + " está indisponível.");
            }
        }

        Optional<Reserva> reservaOptional = reservaRepository
                .findByUsuarioAndSituacao(usuarioBiblioteca.getUsuario(), SituacaoReserva.ATENDIDO_COMPLETAMENTE);

        Reserva reservaAssociada = null;

        if (reservaOptional.isPresent()) {
            Reserva reserva = reservaOptional.get();
            List<UUID> idsReserva = reserva.getExemplares().stream()
                    .map(re -> re.getExemplar().getId()).toList();

            List<UUID> idsEmprestimo = exemplares.stream().map(Exemplar::getId).toList();

            if (new HashSet<>(idsReserva).equals(new HashSet<>(idsEmprestimo))) {
                reserva.setSituacao(SituacaoReserva.CONCLUIDO);
                reservaRepository.save(reserva);
                reservaAssociada = reserva;
            } else {
                throw new BusinessException("Há reservas pendentes com exemplares diferentes.");
            }
        }

        exemplares.forEach(exemplar -> exemplar.setSituacao(SituacaoExemplar.EMPRESTADO));
        exemplarRepository.saveAll(exemplares);

        Emprestimo emprestimo = Emprestimo.builder()
                .biblioteca(biblioteca)
                .usuarioBiblioteca(usuarioBiblioteca)
                .bibliotecario(bibliotecario)
                .situacao(SituacaoEmprestimo.PENDENTE)
                .dataEmissao(LocalDate.now())
                .dataDevolucao(LocalDate.now().plusDays(usuarioBiblioteca.getPerfilUsuario().getPrazoDevolucaoPadrao()))
                .reserva(reservaAssociada)
                .build();

        emprestimoRepository.save(emprestimo);

        List<EmprestimoExemplar> emprestimoExemplares = exemplares.stream().map(exemplar ->
                EmprestimoExemplar.builder()
                        .emprestimo(emprestimo)
                        .exemplar(exemplar)
                        .build()
        ).toList();

        emprestimoExemplarRepository.saveAll(emprestimoExemplares);

        String livroTitulo = exemplares.get(0).getLivro().getTitulo();
        String titulo = "Empréstimo realizado com sucesso";
        String conteudo = "Você retirou o livro '" + livroTitulo + "' na biblioteca '" + biblioteca.getNome() +
                "'. A devolução deve ser feita até " + emprestimo.getDataDevolucao().toString() + ".";

        notificacaoService.notificarUsuario(
                usuarioBiblioteca.getUsuario(),
                titulo,
                conteudo,
                TipoUsuario.COMUM
        );
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

        if (dto.situacao() != null && dto.situacao() != emprestimo.getSituacao()) {
            SituacaoEmprestimo novaSituacao = dto.situacao();
            emprestimo.setSituacao(novaSituacao);

            emprestimoRepository.save(emprestimo);

            if (novaSituacao == SituacaoEmprestimo.CONCLUIDO || novaSituacao == SituacaoEmprestimo.CANCELADO) {
                liberarExemplares(emprestimo);
            }
        } else {
            emprestimoRepository.save(emprestimo);
        }

        return new EmprestimoResponseDTO(
                emprestimo.getId(),
                emprestimo.getDataEmissao(),
                emprestimo.getDataDevolucao(),
                emprestimo.getSituacao(),
                emprestimo.getUsuarioBiblioteca().getId(),
                emprestimo.getBibliotecario().getId(),
                emprestimo.getBiblioteca().getId(),
                emprestimo.getExemplares().stream()
                        .map(ee -> ee.getExemplar().getId())
                        .toList()
        );
    }

    private void liberarExemplares(Emprestimo emprestimo) {
        List<EmprestimoExemplar> emprestimoExemplares = emprestimo.getExemplares();
        List<Exemplar> exemplaresAtualizados = new ArrayList<>();
        List<ReservaExemplar> reservaExemplaresCriados = new ArrayList<>();
        List<Reserva> reservasAtualizadas = new ArrayList<>();

        for (EmprestimoExemplar ee : emprestimoExemplares) {
            Exemplar exemplar = ee.getExemplar();
            boolean foiReservado = false;

            List<Reserva> reservasPendentes = reservaRepository.findReservasPorLivroEBibliotecaComSituacao(
                    exemplar.getLivro().getId(),
                    emprestimo.getBiblioteca().getId(),
                    SituacaoReserva.PENDENTE
            );

            for (Reserva reserva : reservasPendentes) {
                BigDecimal qntPendente = Optional.ofNullable(reserva.getQuantidadePendente())
                        .orElse(BigDecimal.ZERO);

                if (qntPendente.compareTo(BigDecimal.ZERO) > 0) {
                    ReservaExemplar reservaExemplar = ReservaExemplar.builder()
                            .exemplar(exemplar)
                            .reserva(reserva)
                            .build();
                    reservaExemplaresCriados.add(reservaExemplar);

                    reserva.setQuantidadePendente(qntPendente.subtract(BigDecimal.ONE));

                    if (reserva.getQuantidadePendente().compareTo(BigDecimal.ZERO) <= 0) {
                        reserva.setSituacao(SituacaoReserva.ATENDIDO_COMPLETAMENTE);

                        notificacaoService.notificarUsuario(
                                reserva.getUsuario(),
                                "Reserva pronta para retirada",
                                "Sua reserva do livro '" + reserva.getLivro().getTitulo() +
                                        "' na biblioteca '" + reserva.getBiblioteca().getNome() +
                                        "' está disponível para retirada.",
                                TipoUsuario.COMUM
                        );
                    }

                    reservasAtualizadas.add(reserva);
                    foiReservado = true;
                    break;
                }
            }

            exemplar.setSituacao(foiReservado ? SituacaoExemplar.RESERVADO : SituacaoExemplar.DISPONIVEL);
            exemplaresAtualizados.add(exemplar);
        }

        exemplarRepository.saveAll(exemplaresAtualizados);
        reservaRepository.saveAll(reservasAtualizadas);
        reservaExemplarRepository.saveAll(reservaExemplaresCriados);
    }
}
