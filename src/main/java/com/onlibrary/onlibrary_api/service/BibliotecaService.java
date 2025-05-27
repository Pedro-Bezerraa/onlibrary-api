package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.biblioteca.*;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.*;
import com.onlibrary.onlibrary_api.model.enums.ContaSituacao;
import com.onlibrary.onlibrary_api.model.enums.SituacaoEmprestimo;
import com.onlibrary.onlibrary_api.model.enums.TipoUsuario;
import com.onlibrary.onlibrary_api.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BibliotecaService {
    private JwtService jwtService;
    private BibliotecaRepository bibliotecaRepository;
    private PerfilUsuarioRepository perfilUsuarioRepository;
    private UsuarioBibliotecaRepository usuarioBibliotecaRepository;
    private UsuarioRepository usuarioRepository;
    private BibliotecaLivroRepository bibliotecaLivroRepository;
    private ExemplarRepository exemplarRepository;
    private EmprestimoRepository emprestimoRepository;
    private LivroRepository livroRepository;
    private ReservaExemplarRepository reservaExemplarRepository;
    private ReservaRepository reservaRepository;

    public UUID criarBiblioteca(CreateBibliotecaDTO dto, UUID idUsuarioCriador) {
        Biblioteca biblioteca = new Biblioteca();
        biblioteca.setNome(dto.nome());
        biblioteca.setTelefone(dto.telefone());
        biblioteca.setRua(dto.rua());
        biblioteca.setNumero(dto.numero());
        biblioteca.setCep(dto.cep());
        biblioteca.setAplicacaoMulta(dto.aplicacaoMulta());
        biblioteca.setReservaOnline(dto.reservaOnline());
        biblioteca.setAplicacaoBloqueio(dto.aplicacaoBloqueio());

        biblioteca = bibliotecaRepository.save(biblioteca);

        PerfilUsuario perfil = new PerfilUsuario();
        perfil.setNome("bibliotecario");
        perfil.setMultaPadrao(0);
        perfil.setPrazoDevolucaoPadrao(0);
        perfil.setPrazoMultaPadrao(0);
        perfil.setBiblioteca(biblioteca);

        perfil = perfilUsuarioRepository.save(perfil);

        Usuario usuario = usuarioRepository.findById(idUsuarioCriador)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        UsuarioBiblioteca usuarioBiblioteca = new UsuarioBiblioteca();
        usuarioBiblioteca.setBiblioteca(biblioteca);
        usuarioBiblioteca.setUsuario(usuario);
        usuarioBiblioteca.setPerfilUsuario(perfil);
        usuarioBiblioteca.setTipoUsuario((TipoUsuario.ADMIN_MASTER));
        usuarioBiblioteca.setNumeroMatricula("");
        usuarioBiblioteca.setCpf(usuario.getCpf());
        usuarioBiblioteca.setSituacao(ContaSituacao.ATIVO);

        usuarioBibliotecaRepository.save(usuarioBiblioteca);

        return biblioteca.getId();
    }

    public List<CreateBibliotecaResponseDTO> listarBibliotecasAdminOuFuncionario(String token) {
        UUID usuarioId = jwtService.extractIdForUser(token);

        List<UsuarioBiblioteca> vinculos = usuarioBibliotecaRepository
                .findByUsuarioIdAndTipoUsuarioIn(usuarioId,
                        List.of(TipoUsuario.ADMIN_MASTER, TipoUsuario.FUNCIONARIO));

        return vinculos.stream()
                .map(ub -> new CreateBibliotecaResponseDTO(
                        ub.getBiblioteca().getId(),
                        ub.getBiblioteca().getNome(),
                        ub.getBiblioteca().getTelefone(),
                        ub.getBiblioteca().getRua(),
                        ub.getBiblioteca().getNumero(),
                        ub.getBiblioteca().getCep(),
                        ub.getBiblioteca().getAplicacaoMulta(),
                        ub.getBiblioteca().getReservaOnline(),
                        ub.getBiblioteca().getAplicacaoBloqueio()
                ))
                .distinct()
                .collect(Collectors.toList());
    }

    public ContagemResponseDTO contarPorBiblioteca(String type, UUID bibliotecaId) {
        long quantidade = 0;
        boolean aviso = false;

        switch (type.toLowerCase()) {
            case "livro":
                quantidade = bibliotecaLivroRepository.countByBibliotecaId(bibliotecaId);
                break;

            default:
                throw new IllegalArgumentException("Tipo inválido: " + type);
        }
        return new ContagemResponseDTO(quantidade, aviso);
    }

    @Transactional
    public void atualizarExemlar(UUID id, ExemplarRequestDTO dto) {
        Exemplar exemplar = exemplarRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Exemplar não encontrado."));

        if (!exemplar.getDisponivel()) {
            throw new IllegalStateException("Exemplar não está disponível para alteração.");
        }

        exemplar.setLivro(livroRepository.findById(dto.livroId())
                .orElseThrow(() -> new ResourceNotFoundException(("Livro não encntrado"))));
        exemplar.setNumeroTombo(dto.numeroTombo());
        exemplar.setEstante(dto.estante());
        exemplar.setPrateleira(dto.prateleira());
        exemplar.setSetor(dto.setor());
        exemplar.setBiblioteca(bibliotecaRepository.findById(dto.bibliotecaId())
                .orElseThrow(() -> new ResourceNotFoundException("Biblioteca não encontrada")));

        exemplarRepository.save(exemplar);

        Optional<Reserva> reservaOpt = reservaRepository.findFirstBySituacaoAndLivroOrderByDataEmissaoAsc(
                "PENDENTE", exemplar.getLivro());

        if (reservaOpt.isPresent()) {
            Reserva reserva = reservaOpt.get();

            // Cadastrar vínculo na tabela reserva_exemplar
            ReservaExemplar reservaExemplar = ReservaExemplar.builder()
                    .exemplar(exemplar)
                    .reserva(reserva)
                    .build();

            reservaExemplarRepository.save(reservaExemplar);
        }
    }

    public void atualizarEmprestimo(UUID id, EmprestimoRequestDTO dto) {
        Emprestimo emprestimo = emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));

        boolean isFinalizado = emprestimo.getSituacao().equalsIgnoreCase("CONCLUIDO") ||
                emprestimo.getSituacao().equalsIgnoreCase("CANCELADO");

        if (isFinalizado && !emprestimo.getDataDevolucao().equals(dto.dataDevolucao())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Não é permitido alterar a data de devolução de um empréstimo finalizado ou cancelado.");
        }

        emprestimo.setSituacao(dto.situacao());

        if (!isFinalizado) {
            emprestimo.setDataDevolucao(dto.dataDevolucao());
        }

        emprestimoRepository.save(emprestimo);

        if (dto.situacao().equalsIgnoreCase("CONCLUIDO") || dto.situacao().equalsIgnoreCase("CANCELADO")) {

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

    private boolean exemplarDisponivel(Exemplar exemplar) {
        boolean temEmprestimos = emprestimoRepository.existsByExemplarAndSituacao(
                exemplar, SituacaoEmprestimo.CONCLUIDO);

        boolean temReservas = reservaExemplarRepository.existsByExemplar(exemplar);

        return !temEmprestimos && !temReservas;
    }
}
