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
            throw new BusinessException("Usuário bloqueado");
        }

        List<Exemplar> exemplaresDisponiveis = exemplarRepository
                .findByLivroAndBibliotecaAndSituacao(livro, biblioteca, SituacaoExemplar.DISPONIVEL);

        if (exemplaresDisponiveis.isEmpty()) {
            throw new BusinessException("Nenhum exemplar disponível para reserva.");
        }

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

        List<ReservaExemplar> reservasExemplares = new ArrayList<>();

        for (int i = 0; i < quantidadeAtendida; i++) {
            Exemplar exemplar = exemplaresDisponiveis.get(i);

            ReservaExemplar re = new ReservaExemplar();
            re.setReserva(reserva);
            re.setExemplar(exemplar);
            reservasExemplares.add(re);

            exemplar.setSituacao(SituacaoExemplar.RESERVADO);
        }

        reservaExemplarRepository.saveAll(reservasExemplares);
        exemplarRepository.saveAll(exemplaresDisponiveis.subList(0, quantidadeAtendida));

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

        Usuario bibliotecario = usuarioRepository.findById(dto.bibliotecarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Bibliotecário não encontrado"));

        List<ReservaExemplar> exemplares = reservaExemplarRepository.findByReservaIdComExemplar(idReserva);

        if (exemplares.isEmpty()) {
            throw new BusinessException("Nenhum exemplar associado à reserva.");
        }

        boolean todosDisponiveis = exemplares.stream()
                .allMatch(re -> SituacaoExemplar.DISPONIVEL.equals(re.getExemplar().getSituacao()));

        if (!todosDisponiveis) {
            throw new BusinessException("Nem todos os exemplares estão disponíveis para retirada.");
        }

        reserva.setDataRetirada(LocalDate.now());
        reserva.setSituacao(SituacaoReserva.ATENDIDO_COMPLETAMENTE);
        reserva.setBibliotecario(bibliotecario);

        exemplares.forEach(re -> {
            Exemplar exemplar = re.getExemplar();
            exemplar.setSituacao(SituacaoExemplar.RESERVADO);
            exemplarRepository.save(exemplar);
        });

        reservaRepository.save(reserva);

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
}
