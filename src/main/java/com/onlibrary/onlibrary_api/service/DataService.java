package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.graficos.ChartDataResponseDTO;
import com.onlibrary.onlibrary_api.dto.graficos.ChartLastDataDTO;
import com.onlibrary.onlibrary_api.dto.graficos.ChartWeekDataDTO;
import com.onlibrary.onlibrary_api.dto.livro.BookDependenciesDTO;
import com.onlibrary.onlibrary_api.dto.LabelValueDTO;
import com.onlibrary.onlibrary_api.dto.biblioteca.LibraryUserDependenciesDTO;
import com.onlibrary.onlibrary_api.dto.biblioteca.ContagemResponseDTO;
import com.onlibrary.onlibrary_api.exception.BusinessException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.Usuario;
import com.onlibrary.onlibrary_api.model.enums.*;
import com.onlibrary.onlibrary_api.repository.custom.ChartRepository;
import com.onlibrary.onlibrary_api.repository.entities.*;
import com.onlibrary.onlibrary_api.repository.views.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataService {
    private final VwTableUsuarioRepository vwTableUsuarioRepository;
    private final VwTableBibliotecaRepository vwTableBibliotecaRepository;
    private final VwTableLivroRepository vwTableLivroRepository;
    private final VwTableCategoriaRepository vwTableCategoriaRepository;
    private final VwTableGeneroRepository vwTableGeneroRepository;
    private final VwTableEditoraRepository vwTableEditoraRepository;
    private final VwTableAutorRepository vwTableAutorRepository;
    private final VwTableBibliotecaLivroRepository vwTableBibliotecaLivroRepository;
    private final VwTableBibliotecaAutorRepository vwTableBibliotecaAutorRepository;
    private final VwTableBibliotecaGeneroRepository vwTableBibliotecaGeneroRepository;
    private final VwTableBibliotecaEditoraRepository vwTableBibliotecaEditoraRepository;
    private final VwTableExemplarRepository vwTableExemplarRepository;
    private final VwTabelaUsuarioBibliotecaRepository vwTabelaUsuarioBibliotecaRepository;
    private final VwTabelaPerfilUsuarioRepository vwTabelaPerfilUsuarioRepository;
    private final VwTableEmprestimoRepository vwTableEmprestimoRepository;
    private final VwTableMultaRepository vwTableMultaRepository;
    private final VwTableReservaRepository vwTableReservaRepository;
    private final VwTableBibliotecaCategoriaRepository vwTableBibliotecaCategoriaRepository;
    private final LivroRepository livroRepository;
    private final BibliotecaLivroRepository bibliotecaLivroRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final GeneroRepository generoRepository;
    private final EditoraRepository editoraRepository;
    private final PerfilUsuarioRepository perfilUsuarioRepository;
    private final UsuarioBibliotecaRepository usuarioBibliotecaRepository;
    private final EmprestimoRepository emprestimoRepository;
    private final MultaRepository multaRepository;
    private final ExemplarRepository exemplarRepository;
    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final BibliotecaRepository bibliotecaRepository;
    private final ChartRepository chartRepository;

    private static final Set<String> ALLOWED_TABLE_NAMES = Set.of(
            "tb_emprestimo", "tb_reserva", "tb_multa", "tb_usuario", "tb_biblioteca"
    );

    @Transactional(readOnly = true)
    public Map<String, List<?>> getSummary(UUID bibliotecaId, String type) {
        Map<String, List<?>> summary = new HashMap<>();

        if ("biblioteca".equalsIgnoreCase(type)) {
            if (bibliotecaId == null) {
                throw new BusinessException("O ID da biblioteca é obrigatório para o tipo 'biblioteca'.");
            }
            summary.put("livros_da_biblioteca", vwTableBibliotecaLivroRepository.findByFkIdBiblioteca(bibliotecaId));
            summary.put("autores_da_biblioteca", vwTableBibliotecaAutorRepository.findByFkIdBiblioteca(bibliotecaId));
            summary.put("generos_da_biblioteca", vwTableBibliotecaGeneroRepository.findByFkIdBiblioteca(bibliotecaId));
            summary.put("editoras_da_biblioteca", vwTableBibliotecaEditoraRepository.findByFkIdBiblioteca(bibliotecaId));
            summary.put("categorias_da_biblioteca", vwTableBibliotecaCategoriaRepository.findByFkIdBiblioteca(bibliotecaId));
            summary.put("exemplares_da_biblioteca", vwTableExemplarRepository.findByFkIdBiblioteca(bibliotecaId));
            summary.put("usuarios_da_biblioteca", vwTabelaUsuarioBibliotecaRepository.findByFkIdBiblioteca(bibliotecaId));
            summary.put("perfis_da_biblioteca", vwTabelaPerfilUsuarioRepository.findByFkIdBiblioteca(bibliotecaId));
            summary.put("emprestimos_da_biblioteca", vwTableEmprestimoRepository.findByFkIdBiblioteca(bibliotecaId));
            summary.put("multas_da_biblioteca", vwTableMultaRepository.findByFkIdBiblioteca(bibliotecaId));
            summary.put("reservas_da_biblioteca", vwTableReservaRepository.findByFkIdBiblioteca(bibliotecaId));

        } else if ("admin".equalsIgnoreCase(type)) {
            summary.put("livros", vwTableLivroRepository.findAll());
            summary.put("usuarios", vwTableUsuarioRepository.findAll());
            summary.put("bibliotecas", vwTableBibliotecaRepository.findAll());
            summary.put("autores", vwTableAutorRepository.findAll());
            summary.put("categorias", vwTableCategoriaRepository.findAll());
            summary.put("generos", vwTableGeneroRepository.findAll());
            summary.put("editoras", vwTableEditoraRepository.findAll());
        } else {
            throw new BusinessException("Tipo de resumo inválido: " + type);
        }

        return summary;
    }

    @Transactional(readOnly = true)
    public ChartDataResponseDTO getChartData(String nomeTabela, UUID bibliotecaId) {

        Set<String> tabelasDependentes = Set.of("tb_emprestimo", "tb_reserva", "tb_multa");

        if (bibliotecaId == null && tabelasDependentes.contains(nomeTabela)) {
            throw new IllegalArgumentException(
                    "O parâmetro 'id_biblioteca' é obrigatório para a tabela '" + nomeTabela + "'."
            );
        }

        if (!ALLOWED_TABLE_NAMES.contains(nomeTabela)) {
            throw new IllegalArgumentException("Nome de tabela inválido: " + nomeTabela);
        }

        List<ChartWeekDataDTO> weekData = chartRepository.getWeekData(nomeTabela, bibliotecaId);
        List<ChartLastDataDTO> lastData = chartRepository.getLastData(nomeTabela, bibliotecaId);

        return new ChartDataResponseDTO(weekData, lastData);
    }

    public Object getGroupData(String type, UUID bibliotecaId) {
        switch (type.toLowerCase()) {
            case "book": {
                var autores = autorRepository.findByDeletadoFalse().stream()
                        .map(a -> new LabelValueDTO(a.getNome(), a.getId())).collect(Collectors.toList());
                var categorias = categoriaRepository.findByDeletadoFalse().stream()
                        .map(c -> new LabelValueDTO(c.getNome(), c.getId())).collect(Collectors.toList());
                var generos = generoRepository.findByDeletadoFalse().stream()
                        .map(g -> new LabelValueDTO(g.getNome(), g.getId())).collect(Collectors.toList());
                var editoras = editoraRepository.findByDeletadoFalse().stream()
                        .map(e -> new LabelValueDTO(e.getNome(), e.getId())).collect(Collectors.toList());
                return new BookDependenciesDTO(autores, categorias, generos, editoras);
            }

            case "library_user": {
                if (bibliotecaId == null) throw new IllegalArgumentException("id_biblioteca é obrigatório");
                var usuarios = usuarioRepository.findUsersNotInLibrary(bibliotecaId).stream()
                        .map(u -> new LabelValueDTO(u.getUsername(), u.getId())).collect(Collectors.toList());
                var perfis = perfilUsuarioRepository.findByBibliotecaIdAndDeletadoFalse(bibliotecaId).stream()
                        .map(p -> new LabelValueDTO(p.getNome(), p.getId())).collect(Collectors.toList());
                return new LibraryUserDependenciesDTO(usuarios, perfis);
            }

            case "exemplary": {
                var livros = livroRepository.findByDeletadoFalse().stream()
                        .map(l -> new LabelValueDTO(l.getTitulo() + " (ISBN: " + l.getIsbn() + ")", l.getId()))
                        .collect(Collectors.toList());
                return java.util.Map.of("livros_biblioteca", livros);
            }

            case "loan": {
                if (bibliotecaId == null) throw new IllegalArgumentException("id_biblioteca é obrigatório");
                var usuarios = usuarioBibliotecaRepository.findCommonAndActiveUsersByBibliotecaId(bibliotecaId).stream()
                        .map(ub -> new LabelValueDTO(ub.getUsuario().getUsername(), ub.getId()))
                        .collect(Collectors.toList());
                var exemplares = exemplarRepository.findByBibliotecaIdAndSituacaoInAndDeletadoFalse(
                                bibliotecaId, List.of(SituacaoExemplar.DISPONIVEL, SituacaoExemplar.RESERVADO)
                        ).stream()
                        .map(e -> new LabelValueDTO(e.getNumeroTombo(), e.getId()))
                        .collect(Collectors.toList());
                return java.util.Map.of("exemplares_biblioteca", exemplares, "usuarios_biblioteca", usuarios);
            }

            case "reserve": {
                if (bibliotecaId == null) throw new IllegalArgumentException("id_biblioteca é obrigatório");
                var usuarios = usuarioRepository.findByDeletadoFalse().stream()
                        .map(u -> new LabelValueDTO(u.getUsername(), u.getId()))
                        .collect(Collectors.toList());
                var livros = bibliotecaLivroRepository.findWithLivroByBibliotecaId(bibliotecaId).stream()
                        .map(bl -> new LabelValueDTO(bl.getLivro().getTitulo(), bl.getLivro().getId()))
                        .collect(Collectors.toList());
                return java.util.Map.of("usuarios_biblioteca", usuarios, "livros_biblioteca", livros);
            }

            case "amerce": {
                if (bibliotecaId == null) throw new IllegalArgumentException("id_biblioteca é obrigatório");
                var usuarios = usuarioBibliotecaRepository.findCommonAndActiveUsersByBibliotecaId(bibliotecaId).stream()
                        .map(ub -> new LabelValueDTO(ub.getUsuario().getUsername(), ub.getUsuario().getId()))
                        .collect(Collectors.toList());
                return java.util.Map.of("usuarios_biblioteca", usuarios);
            }

            default:
                throw new IllegalArgumentException("O tipo de agrupamento '" + type + "' é inválido.");
        }
    }

    public ContagemResponseDTO getCount(String type, UUID bibliotecaId, UUID usuarioId) {
        long quantidade = 0;
        boolean aviso = false;

        switch (type.toLowerCase()) {
            case "library":
                quantidade = bibliotecaRepository.countByDeletadoFalse();
                break;
            case "book":
                quantidade = livroRepository.countByDeletadoFalse();
                break;
            case "library_book":
                quantidade = bibliotecaLivroRepository.countByBibliotecaIdAndDeletadoFalse(bibliotecaId);
                break;
            case "author":
                quantidade = autorRepository.countByDeletadoFalse();
                break;
            case "library_author":
                if (bibliotecaId == null) throw new IllegalArgumentException("id_biblioteca é obrigatório para o tipo 'library_author'");
                quantidade = autorRepository.countByBibliotecaId(bibliotecaId);
                break;
            case "category":
                quantidade = categoriaRepository.countByDeletadoFalse();
                break;
            case "library_category":
                if (bibliotecaId == null) throw new IllegalArgumentException("id_biblioteca é obrigatório para o tipo 'library_category'");
                quantidade = categoriaRepository.countByBibliotecaId(bibliotecaId);
                break;
            case "gender":
                quantidade = generoRepository.countByDeletadoFalse();
                break;
            case "library_gender":
                if (bibliotecaId == null) throw new IllegalArgumentException("id_biblioteca é obrigatório para o tipo 'library_gender'");
                quantidade = generoRepository.countByBibliotecaId(bibliotecaId);
                break;
            case "publisher":
                quantidade = editoraRepository.countByDeletadoFalse();
                break;
            case "library_publisher":
                if (bibliotecaId == null) throw new IllegalArgumentException("id_biblioteca é obrigatório para o tipo 'library_publisher'");
                quantidade = editoraRepository.countByBibliotecaId(bibliotecaId);
                break;
            case "account":
                quantidade = perfilUsuarioRepository.countByBibliotecaIdAndDeletadoFalse(bibliotecaId);
                break;
            case "exemplary":
                quantidade = exemplarRepository.countByBibliotecaIdAndDeletadoFalse(bibliotecaId);
                break;
            case "user":
                quantidade = usuarioRepository.countByDeletadoFalse();
                break;

            case "library_user":
                if (usuarioId == null) throw new IllegalArgumentException("id_usuario é obrigatório para o tipo 'library_user'");
                Usuario caller = usuarioRepository.findById(usuarioId)
                        .orElseThrow(() -> new ResourceNotFoundException("Usuário que fez a chamada não encontrado."));
                if (caller.getTipo() == TipoUsuario.ADMIN_MASTER) {
                    quantidade = usuarioBibliotecaRepository.countByBibliotecaIdAndDeletadoFalse(bibliotecaId);
                } else {
                    quantidade = usuarioBibliotecaRepository.countComumUsersByBiblioteca(bibliotecaId);
                }
                break;

            case "loan":
                quantidade = emprestimoRepository.countByBibliotecaIdAndDeletadoIsFalse(bibliotecaId);
                aviso = emprestimoRepository.existsByBibliotecaIdAndSituacaoAndDataDevolucaoBefore(
                        bibliotecaId, SituacaoEmprestimo.PENDENTE, LocalDate.now()
                );
                break;

            case "amerce":
                quantidade = multaRepository.countByBibliotecaIdAndDeletadoFalse(bibliotecaId);
                aviso = multaRepository.existsByBibliotecaIdAndSituacaoAndDataVencimentoBefore(
                        bibliotecaId, SituacaoMulta.PENDENTE, LocalDate.now()
                );
                break;

            case "reserve":
                quantidade = reservaRepository.countByBibliotecaIdAndDeletadoIsFalse(bibliotecaId);
                aviso = reservaRepository.existsByBibliotecaIdAndSituacaoIn(
                        bibliotecaId, List.of(SituacaoReserva.ATENDIDO_PARCIALMENTE, SituacaoReserva.ATENDIDO_COMPLETAMENTE)
                );
                break;

            case "online_reserve":
                quantidade = reservaRepository.countByBibliotecaIdAndTipoAndDeletadoIsFalse(bibliotecaId, TipoReserva.ONLINE);
                aviso = reservaRepository.existsByBibliotecaIdAndTipoAndSituacaoIn(
                        bibliotecaId, TipoReserva.ONLINE, List.of(SituacaoReserva.ATENDIDO_PARCIALMENTE, SituacaoReserva.ATENDIDO_COMPLETAMENTE)
                );
                break;

            default:
                throw new IllegalArgumentException("O tipo de contagem '" + type + "' é inválido.");
        }
        return new ContagemResponseDTO(quantidade, aviso);
    }

    public List<?> getData(String type, UUID bibliotecaId) {
        switch (type.toLowerCase()) {
            case "user":
                return vwTableUsuarioRepository.findAll();
            case "library":
                return vwTableBibliotecaRepository.findAll();
            case "book":
                return vwTableLivroRepository.findAll();
            case "category":
                return vwTableCategoriaRepository.findAll();
            case "gender":
                return vwTableGeneroRepository.findAll();
            case "publisher":
                return vwTableEditoraRepository.findAll();
            case "author":
                return vwTableAutorRepository.findAll();

            case "library_book":
                return vwTableBibliotecaLivroRepository.findByFkIdBiblioteca(bibliotecaId);
            case "library_author":
                return vwTableBibliotecaAutorRepository.findByFkIdBiblioteca(bibliotecaId);
            case "library_gender":
                return vwTableBibliotecaGeneroRepository.findByFkIdBiblioteca(bibliotecaId);
            case "library_publisher":
                return vwTableBibliotecaEditoraRepository.findByFkIdBiblioteca(bibliotecaId);
            case "exemplary":
                return vwTableExemplarRepository.findByFkIdBiblioteca(bibliotecaId);
            case "library_user":
                return vwTabelaUsuarioBibliotecaRepository.findByFkIdBiblioteca(bibliotecaId);
            case "account":
                return vwTabelaPerfilUsuarioRepository.findByFkIdBiblioteca(bibliotecaId);
            case "loan":
                return vwTableEmprestimoRepository.findByFkIdBiblioteca(bibliotecaId);
            case "amerce":
                return vwTableMultaRepository.findByFkIdBiblioteca(bibliotecaId);
            case "reserve":
                return vwTableReservaRepository.findByFkIdBiblioteca(bibliotecaId);
            case "library_category":
                return vwTableBibliotecaCategoriaRepository.findByFkIdBiblioteca(bibliotecaId);

            case "online_reserve":
                return vwTableReservaRepository.findByFkIdBibliotecaAndTipo(bibliotecaId, "ONLINE");

            default:
                throw new IllegalArgumentException("O tipo de dado '" + type + "' é inválido.");
        }
    }
}
