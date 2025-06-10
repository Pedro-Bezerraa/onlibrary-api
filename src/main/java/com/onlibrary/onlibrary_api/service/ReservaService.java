package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.reserva.AttReservaRequestDTO;
import com.onlibrary.onlibrary_api.dto.reserva.AttReservaResponseDTO;
import com.onlibrary.onlibrary_api.dto.reserva.ReservaRequestDTO;
import com.onlibrary.onlibrary_api.dto.reserva.ReservaResponseDTO;
import com.onlibrary.onlibrary_api.exception.BusinessException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.*;
import com.onlibrary.onlibrary_api.model.enums.ContaSituacao;
import com.onlibrary.onlibrary_api.model.enums.SituacaoExemplar;
import com.onlibrary.onlibrary_api.model.enums.SituacaoReserva;
import com.onlibrary.onlibrary_api.model.enums.TipoUsuario;
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
public class ReservaService {
    private final NotificacaoService notificacaoService;
    private final ReservaRepository reservaRepository;
    private final LivroRepository livroRepository;
    private final ExemplarRepository exemplarRepository;
    private final ReservaExemplarRepository reservaExemplarRepository;
    private final BibliotecaRepository bibliotecaRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioBibliotecaRepository usuarioBibliotecaRepository;


    @Transactional
    public ReservaResponseDTO criarReserva(ReservaRequestDTO dto) {
        Biblioteca biblioteca = bibliotecaRepository.findById(dto.bibliotecaId())
                .orElseThrow(() -> new ResourceNotFoundException("Biblioteca não encontrada"));

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Usuario bibliotecario = null;
        if (dto.bibliotecarioId() != null) {
            bibliotecario = usuarioRepository.findById(dto.bibliotecarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bibliotecário não encontrado"));
        }

        Livro livro = livroRepository.findById(dto.livroId())
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado"));

        if (usuario.getSituacao() == ContaSituacao.BLOQUEADO) {
            throw new BusinessException("Usuário bloqueado no sistema.");
        }

        usuarioBibliotecaRepository.findByUsuarioIdAndBibliotecaId(usuario.getId(), biblioteca.getId())
                .ifPresent(usuarioBiblioteca -> {
                    if (usuarioBiblioteca.getSituacao() == ContaSituacao.BLOQUEADO) {
                        throw new BusinessException("O usuário está bloqueado nesta biblioteca e não pode fazer reservas.");
                    }
                });

        List<Exemplar> exemplaresDisponiveis = exemplarRepository
                .findByLivroAndBibliotecaAndSituacao(livro, biblioteca, SituacaoExemplar.DISPONIVEL);

        int quantidadeSolicitada = dto.quantidade().intValue();
        int quantidadeDisponivel = exemplaresDisponiveis.size();
        int quantidadeAtendida = Math.min(quantidadeSolicitada, quantidadeDisponivel);
        BigDecimal quantidadePendente = BigDecimal.valueOf(quantidadeSolicitada - quantidadeAtendida);

        SituacaoReserva situacao;

        if (quantidadePendente.compareTo(BigDecimal.ZERO) > 0) {
            situacao = SituacaoReserva.PENDENTE;
        } else {
            situacao = SituacaoReserva.ATENDIDO_PARCIALMENTE;
        }

        Reserva reserva = Reserva.builder()
                .biblioteca(biblioteca)
                .usuario(usuario)
                .bibliotecario(bibliotecario)
                .livro(livro)
                .dataEmissao(LocalDate.now())
                .dataRetirada(null)
                .situacao(situacao)
                .tipo(dto.tipo())
                .quantidadeTotal(dto.quantidade())
                .quantidadePendente(quantidadePendente)
                .build();

        reservaRepository.save(reserva);

        String titulo = "Reserva solicitada com sucesso";
        String conteudo = "Sua solicitação de reserva do livro '" + livro.getTitulo() +
                "' na biblioteca '" + biblioteca.getNome() +
                "' foi realizada com sucesso. Aguarde a data de retirada.";

        notificacaoService.notificarUsuario(
                usuario,
                titulo,
                conteudo,
                TipoUsuario.COMUM
        );

        List<ReservaExemplar> reservasExemplares = new ArrayList<>();

        for (int i = 0; i < quantidadeAtendida; i++) {
            Exemplar exemplar = exemplaresDisponiveis.get(i);

            ReservaExemplar re = new ReservaExemplar();
            re.setReserva(reserva);
            re.setExemplar(exemplar);
            reservasExemplares.add(re);

            exemplar.setSituacao(SituacaoExemplar.RESERVADO);
        }

        if (!reservasExemplares.isEmpty()) {
            reservaExemplarRepository.saveAll(reservasExemplares);
        }
        if (quantidadeAtendida > 0) {
            exemplarRepository.saveAll(exemplaresDisponiveis.subList(0, quantidadeAtendida));
        }


        return new ReservaResponseDTO(
                reserva.getId(),
                biblioteca.getId(),
                usuario.getId(),
                reserva.getDataEmissao(),
                reserva.getDataRetirada(),
                reserva.getSituacao(),
                reserva.getTipo(),
                reserva.getQuantidadeTotal(),
                reserva.getQuantidadePendente(),
                livro.getId()
        );
    }

    @Transactional
    public AttReservaResponseDTO atualizarReserva(UUID idReserva, AttReservaRequestDTO dto) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada"));

        if (dto.situacao() != null && dto.situacao() == SituacaoReserva.CANCELADO) {
            boolean hasPendingEmprestimos = reservaRepository.hasPendingEmprestimosByReservaId(idReserva);
            if (hasPendingEmprestimos) {
                throw new BusinessException("Não é possível cancelar a reserva: Há um empréstimo ativo associado a ela.");
            }

            reserva.setSituacao(SituacaoReserva.CANCELADO);

            List<ReservaExemplar> reservasExemplares = reservaExemplarRepository.findByReservaIdComExemplar(idReserva);

            for (ReservaExemplar re : reservasExemplares) {
                Exemplar exemplar = re.getExemplar();
                atenderReservasPendentesAposCancelamento(exemplar);
            }

            reservaExemplarRepository.deleteAll(reservasExemplares);

            notificacaoService.notificarUsuario(
                    reserva.getUsuario(),
                    "Reserva Cancelada",
                    "Sua reserva do livro '" + reserva.getLivro().getTitulo() +
                            "' na biblioteca '" + reserva.getBiblioteca().getNome() +
                            "' foi cancelada.",
                    TipoUsuario.COMUM
            );
        }

        if (dto.dataRetirada() != null) {
            reserva.setDataRetirada(dto.dataRetirada());

            List<ReservaExemplar> exemplares = reservaExemplarRepository.findByReservaIdComExemplar(idReserva);

            if (exemplares.isEmpty()) {
                throw new BusinessException("Nenhum exemplar associado à reserva.");
            }

            boolean todosReservados = exemplares.stream()
                    .allMatch(re -> SituacaoExemplar.RESERVADO.equals(re.getExemplar().getSituacao()));

            if (!todosReservados) {
                throw new BusinessException("Nem todos os exemplares estão no estado correto (RESERVADO) para confirmar a retirada.");
            }

            if (reserva.getQuantidadePendente() == null || reserva.getQuantidadePendente().compareTo(BigDecimal.ZERO) <= 0) {
                reserva.setSituacao(SituacaoReserva.ATENDIDO_COMPLETAMENTE);
            }

            String titulo = "Reserva pronta para retirada";
            String conteudo = "Sua reserva do livro '" + reserva.getLivro().getTitulo() +
                    "' na biblioteca '" + reserva.getBiblioteca().getNome() +
                    "' está disponível para retirada.";

            notificacaoService.notificarUsuario(
                    reserva.getUsuario(),
                    titulo,
                    conteudo,
                    TipoUsuario.COMUM
            );
        }

        if (dto.bibliotecarioId() != null) {
            Usuario bibliotecario = usuarioRepository.findById(dto.bibliotecarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Bibliotecário não encontrado"));
            reserva.setBibliotecario(bibliotecario);
        }

        reservaRepository.save(reserva);

        return new AttReservaResponseDTO(
                reserva.getId(),
                reserva.getBiblioteca().getId(),
                reserva.getUsuario().getId(),
                reserva.getDataEmissao(),
                reserva.getDataRetirada(),
                reserva.getSituacao(),
                reserva.getTipo(),
                reserva.getQuantidadeTotal(),
                reserva.getQuantidadePendente(),
                reserva.getLivro().getId()
        );
    }

    @Transactional
    public void deletarReserva(UUID idReserva) {
        Reserva reserva = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada."));

        if (reserva.getSituacao() == SituacaoReserva.PENDENTE ||
                reserva.getSituacao() == SituacaoReserva.ATENDIDO_PARCIALMENTE ||
                reserva.getSituacao() == SituacaoReserva.ATENDIDO_COMPLETAMENTE) {

            boolean hasPendingEmprestimos = reservaRepository.hasPendingEmprestimosByReservaId(idReserva);
            if (hasPendingEmprestimos) {
//                notificacaoService.notificarUsuario(
//                        reserva.getUsuario(),
//                        "Não foi possível excluir a reserva",
//                        "Sua reserva do livro '" + reserva.getLivro().getTitulo() +
//                                "' na biblioteca '" + reserva.getBiblioteca().getNome() +
//                                "' não pode ser excluída pois há um empréstimo ativo associado a ela.",
//                        TipoUsuario.COMUM
//                );
                throw new BusinessException("Não é possível excluir a reserva: Há um empréstimo ativo associado a ela.");
            }

//            notificacaoService.notificarUsuario(
//                    reserva.getUsuario(),
//                    "Não foi possível excluir a reserva",
//                    "Sua reserva do livro '" + reserva.getLivro().getTitulo() +
//                            "' na biblioteca '" + reserva.getBiblioteca().getNome() +
//                            "' não pode ser excluída pois está em andamento (Pendente ou Atendida).",
//                    TipoUsuario.COMUM
//            );
            throw new BusinessException("Não é possível excluir uma reserva com situação em andamento");
        }

        reserva.setDeletado(true);
        reservaRepository.save(reserva);

//        notificacaoService.notificarUsuario(
//                reserva.getUsuario(),
//                "Reserva arquivada",
//                "Sua reserva do livro '" + reserva.getLivro().getTitulo() +
//                        "' na biblioteca '" + reserva.getBiblioteca().getNome() +
//                        "' foi arquivada do sistema. Não há mais pendências associadas a ela.",
//                TipoUsuario.COMUM
//        );
    }

    private void atenderReservasPendentesAposCancelamento(Exemplar exemplar) {
        List<Reserva> proximasReservas = reservaRepository.findReservasPorLivroEBibliotecaComSituacao(
                exemplar.getLivro().getId(),
                exemplar.getBiblioteca().getId(),
                SituacaoReserva.PENDENTE
        );

        boolean exemplarReatribuido = false;
        if (!proximasReservas.isEmpty()) {
            Reserva proximaReserva = proximasReservas.get(0);

            ReservaExemplar novaReservaExemplar = new ReservaExemplar();
            novaReservaExemplar.setReserva(proximaReserva);
            novaReservaExemplar.setExemplar(exemplar);
            reservaExemplarRepository.save(novaReservaExemplar);

            proximaReserva.setQuantidadePendente(proximaReserva.getQuantidadePendente().subtract(BigDecimal.ONE));
            if (proximaReserva.getQuantidadePendente().compareTo(BigDecimal.ZERO) <= 0) {
                proximaReserva.setSituacao(SituacaoReserva.ATENDIDO_PARCIALMENTE);
            }
            reservaRepository.save(proximaReserva);

            exemplar.setSituacao(SituacaoExemplar.RESERVADO);
            exemplarReatribuido = true;
        }

        if (!exemplarReatribuido) {
            exemplar.setSituacao(SituacaoExemplar.DISPONIVEL);
        }

        exemplarRepository.save(exemplar);
    }
}
