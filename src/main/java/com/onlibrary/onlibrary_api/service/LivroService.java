package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.LabelValueDTO;
import com.onlibrary.onlibrary_api.dto.livro.*;
import com.onlibrary.onlibrary_api.dto.autor.AutorResponseDTO;
import com.onlibrary.onlibrary_api.dto.categoria.CategoriaResponseDTO;
import com.onlibrary.onlibrary_api.dto.editora.EditoraResponseDTO;
import com.onlibrary.onlibrary_api.dto.genero.GeneroResponseDTO;
import com.onlibrary.onlibrary_api.exception.BusinessException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.*;
import com.onlibrary.onlibrary_api.model.views.VwBibliotecaReservaExemplar;
import com.onlibrary.onlibrary_api.model.views.VwLivro;
import com.onlibrary.onlibrary_api.model.views.VwTableBibliotecaLivro;
import com.onlibrary.onlibrary_api.repository.entities.*;
import com.onlibrary.onlibrary_api.repository.views.VwBibliotecaReservaExemplarRepository;
import com.onlibrary.onlibrary_api.repository.views.VwLivroRepository;
import com.onlibrary.onlibrary_api.repository.views.VwTableBibliotecaLivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.onlibrary.onlibrary_api.repository.views.VwLivroRepository.SuggestionProjection;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LivroService {
    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;
    private final CategoriaRepository categoriaRepository;
    private final GeneroRepository generoRepository;
    private final EditoraRepository editoraRepository;
    private final LivroAutorRepository livroAutorRepository;
    private final LivroCategoriaRepository livroCategoriaRepository;
    private final LivroGeneroRepository livroGeneroRepository ;
    private final LivroEditoraRepository livroEditoraRepository ;
    private final SupabaseStorageService supabaseStorageService;
    private final VwLivroRepository vwLivroRepository;
    private final VwBibliotecaReservaExemplarRepository vwBibliotecaReservaExemplarRepository;
    private final VwTableBibliotecaLivroRepository vwTableBibliotecaLivroRepository;

    @Transactional(readOnly = true)
    public BookPageDTO getBookPageInfo(UUID livroId) {
        VwLivro livro = vwLivroRepository.findById(livroId)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado"));

        return new BookPageDTO(
                livro.getId(),
                livro.getIsbn(),
                livro.getTitulo(),
                livro.getDescricao(),
                livro.getCapa(),
                livro.getAnoLancamento(),
                livro.getAutores(),
                livro.getCategorias(),
                livro.getGeneros(),
                livro.getEditoras()
        );
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getLivroDependenciesForUpdate(UUID livroId) {
        Livro livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado"));

        Map<String, Object> dependencies = new HashMap<>();
        dependencies.put("ISBN", livro.getIsbn());
        dependencies.put("imagem", livro.getCapa());
        dependencies.put("titulo", livro.getTitulo());
        dependencies.put("descricao", livro.getDescricao());
        dependencies.put("ano_lancamento", livro.getAnoLancamento());

        dependencies.put("categorias", livro.getCategorias().stream()
                .map(livroCategoria -> new LabelValueDTO(livroCategoria.getCategoria().getNome(), livroCategoria.getCategoria().getId()))
                .collect(Collectors.toList()));
        dependencies.put("generos", livro.getGeneros().stream()
                .map(livroGenero -> new LabelValueDTO(livroGenero.getGenero().getNome(), livroGenero.getGenero().getId()))
                .collect(Collectors.toList()));
        dependencies.put("editoras", livro.getEditoras().stream()
                .map(livroEditora -> new LabelValueDTO(livroEditora.getEditora().getNome(), livroEditora.getEditora().getId()))
                .collect(Collectors.toList()));
        dependencies.put("autores", livro.getAutores().stream()
                .map(livroAutor -> new LabelValueDTO(livroAutor.getAutor().getNome(), livroAutor.getAutor().getId()))
                .collect(Collectors.toList()));

        return dependencies;
    }

    @Transactional(readOnly = true)
    public List<SuggestionProjection> getSearchSuggestions(String value) {
        if (value == null || value.trim().isEmpty()) {
            return new ArrayList<>();
        }
        return vwLivroRepository.getSearchSuggestions(value);
    }

    @Transactional(readOnly = true)
    public List<VwTableBibliotecaLivro> searchLivrosInBiblioteca(String value, String filter, UUID bibliotecaId) {
        if (value == null || value.trim().isEmpty()) {
            return vwTableBibliotecaLivroRepository.findByFkIdBiblioteca(bibliotecaId);
        }

        return switch (filter.toLowerCase()) {
            case "titulo" -> vwTableBibliotecaLivroRepository.searchByTituloInBiblioteca(bibliotecaId, value);
            case "isbn" -> vwTableBibliotecaLivroRepository.searchByIsbnInBiblioteca(bibliotecaId, value);
            case "todos" -> vwTableBibliotecaLivroRepository.searchByAllInBiblioteca(bibliotecaId, value);
            default -> new ArrayList<>();
        };
    }

    @Transactional(readOnly = true)
    public List<LivroHomePageSearchDTO> searchLivrosHomePage(String value, String filter) {
        if (value == null || value.trim().isEmpty()) {
            return vwLivroRepository.findAllAsHomePageSearchDTO();
        }

        return switch (filter.toLowerCase()) {
            case "titulo" -> vwLivroRepository.searchByTitulo(value);
            case "isbn" -> vwLivroRepository.searchByIsbn(value);
            case "autor" -> vwLivroRepository.searchByAutores(value);
            case "categoria" -> vwLivroRepository.searchByCategorias(value);
            case "genero" -> vwLivroRepository.searchByGeneros(value);
            case "editora" -> vwLivroRepository.searchByEditoras(value);
            case "todos" -> vwLivroRepository.searchByAll(value);
            default -> new ArrayList<>();
        };
    }

    @Transactional(readOnly = true)
    public List<VwBibliotecaReservaExemplar> getBibliotecasForLivro(UUID livroId) {
        return vwBibliotecaReservaExemplarRepository.findByFkIdLivro(livroId);
    }

    @Transactional(readOnly = true)
    public VwLivro getLivroDetails(UUID livroId) {
        return vwLivroRepository.findById(livroId)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado."));
    }

    @Transactional
    public LivroResponseDTO criarLivro(LivroRequestDTO dto, MultipartFile capa) {
        if (livroRepository.existsByIsbnIgnoreCase(dto.isbn())) {
            throw new BusinessException("ISBN já cadastrado");
        }

        if (livroRepository.existsByTituloIgnoreCase(dto.titulo())) {
            throw new BusinessException("Título já cadastrado.");
        }

        String urlImagem = null;
        if (capa != null && !capa.isEmpty()) {
            urlImagem = supabaseStorageService.uploadImagem(capa);
        }

        Livro livro = new Livro();

        livro.setIsbn(dto.isbn());
        livro.setTitulo(dto.titulo());
        livro.setDescricao(dto.descricao());
        livro.setAnoLancamento(dto.anoLancamento());
        livro.setCapa(urlImagem);

        livroRepository.save(livro);


        List<LivroAutor> autores = dto.autores().stream()
                .map(autorId -> {
                    Autor autor = autorRepository.findById(autorId)
                            .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado"));
                    return LivroAutor.builder()
                            .livro(livro)
                            .autor(autor)
                            .build();
                })
                .collect(Collectors.toList());

        livroAutorRepository.saveAll(autores);

        List<LivroCategoria> categorias = dto.categorias().stream()
                .map(categoriaId -> {
                    Categoria categoria = categoriaRepository.findById(categoriaId)
                            .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrado"));
                    return LivroCategoria.builder()
                            .livro(livro)
                            .categoria(categoria)
                            .build();
                })
                .collect(Collectors.toList());

        livroCategoriaRepository.saveAll(categorias);

        List<LivroEditora> editoras = dto.editoras().stream()
                .map(editoraId -> {
                    Editora editora = editoraRepository.findById(editoraId)
                            .orElseThrow(() -> new ResourceNotFoundException("Editora não encontrado"));
                    return LivroEditora.builder()
                            .livro(livro)
                            .editora(editora)
                            .build();
                })
                .collect(Collectors.toList());

        livroEditoraRepository.saveAll(editoras);

        List<LivroGenero> generos = dto.generos().stream()
                .map(generoId -> {
                    Genero genero = generoRepository.findById(generoId)
                            .orElseThrow(() -> new ResourceNotFoundException("Genero não encontrado"));
                    return LivroGenero.builder()
                            .livro(livro)
                            .genero(genero)
                            .build();
                })
                .collect(Collectors.toList());

        livroGeneroRepository.saveAll(generos);

        List<AutorResponseDTO> autoresDTO = autores.stream()
                .map(la -> new AutorResponseDTO(la.getAutor().getId(), la.getAutor().getNome()))
                .collect(Collectors.toList());

        List<CategoriaResponseDTO> categoriasDTO = categorias.stream()
                .map(lc -> new CategoriaResponseDTO(lc.getCategoria().getId(), lc.getCategoria().getNome()))
                .collect(Collectors.toList());

        List<EditoraResponseDTO> editorasDTO = editoras.stream()
                .map(le -> new EditoraResponseDTO(le.getEditora().getId(), le.getEditora().getNome()))
                .collect(Collectors.toList());

        List<GeneroResponseDTO> generosDTO = generos.stream()
                .map(lg -> new GeneroResponseDTO(lg.getGenero().getId(), lg.getGenero().getNome()))
                .collect(Collectors.toList());

        return new LivroResponseDTO(
                livro.getId(),
                livro.getIsbn(),
                livro.getTitulo(),
                livro.getDescricao(),
                livro.getAnoLancamento(),
                autoresDTO,
                categoriasDTO,
                generosDTO,
                editorasDTO
        );
    }

    @Transactional
    public UpdateLivroResponseDTO atualizarLivro(UUID id, UpdateLivroRequestDTO dto, MultipartFile capa) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado"));

        if (capa != null && !capa.isEmpty()) {
            String novaImagem = supabaseStorageService.uploadImagem(capa);
            livro.setCapa(novaImagem);
        }

        if (dto != null) {
            if (dto.isbn() != null && !dto.isbn().equalsIgnoreCase(livro.getIsbn())) {
                if (livroRepository.existsByIsbnIgnoreCase(dto.isbn())) {
                    throw new BusinessException("ISBN já cadastrado");
                }
                livro.setIsbn(dto.isbn());
            }

            if (dto.titulo() != null && !dto.titulo().equalsIgnoreCase(livro.getTitulo())) {
                if (livroRepository.existsByTituloIgnoreCase(dto.titulo())) {
                    throw new BusinessException("Título já cadastrado");
                }
                livro.setTitulo(dto.titulo());
            }

            if (dto.descricao() != null) {
                livro.setDescricao(dto.descricao());
            }

            if (dto.anoLancamento() != null) {
                livro.setAnoLancamento(dto.anoLancamento());
            }

            if (dto.autores() != null) {
                livroAutorRepository.deleteByLivroId(livro.getId());
                List<LivroAutor> autores = dto.autores().stream()
                        .map(autorId -> {
                            Autor autor = autorRepository.findById(autorId)
                                    .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado"));
                            return LivroAutor.builder().livro(livro).autor(autor).build();
                        }).toList();
                livroAutorRepository.saveAll(autores);
            }

            if (dto.categorias() != null) {
                livroCategoriaRepository.deleteByLivroId(livro.getId());
                List<LivroCategoria> categorias = dto.categorias().stream()
                        .map(categoriaId -> {
                            Categoria categoria = categoriaRepository.findById(categoriaId)
                                    .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
                            return LivroCategoria.builder().livro(livro).categoria(categoria).build();
                        }).toList();
                livroCategoriaRepository.saveAll(categorias);
            }

            if (dto.editoras() != null) {
                livroEditoraRepository.deleteByLivroId(livro.getId());
                List<LivroEditora> editoras = dto.editoras().stream()
                        .map(editoraId -> {
                            Editora editora = editoraRepository.findById(editoraId)
                                    .orElseThrow(() -> new ResourceNotFoundException("Editora não encontrada"));
                            return LivroEditora.builder().livro(livro).editora(editora).build();
                        }).toList();
                livroEditoraRepository.saveAll(editoras);
            }

            if (dto.generos() != null) {
                livroGeneroRepository.deleteByLivroId(livro.getId());
                List<LivroGenero> generos = dto.generos().stream()
                        .map(generoId -> {
                            Genero genero = generoRepository.findById(generoId)
                                    .orElseThrow(() -> new ResourceNotFoundException("Gênero não encontrado"));
                            return LivroGenero.builder().livro(livro).genero(genero).build();
                        }).toList();
                livroGeneroRepository.saveAll(generos);
            }
        }

        Livro livroSalvo = livroRepository.save(livro);

        return new UpdateLivroResponseDTO(
                livroSalvo.getId(),
                livroSalvo.getIsbn(),
                livroSalvo.getTitulo(),
                livroSalvo.getDescricao(),
                livroSalvo.getAnoLancamento(),
                livroSalvo.getAutores().stream()
                        .map(a -> new AutorResponseDTO(a.getAutor().getId(), a.getAutor().getNome()))
                        .toList(),
                livroSalvo.getCategorias().stream()
                        .map(c -> new CategoriaResponseDTO(c.getCategoria().getId(), c.getCategoria().getNome()))
                        .toList(),
                livroSalvo.getGeneros().stream()
                        .map(g -> new GeneroResponseDTO(g.getGenero().getId(), g.getGenero().getNome()))
                        .toList(),
                livroSalvo.getEditoras().stream()
                        .map(e -> new EditoraResponseDTO(e.getEditora().getId(), e.getEditora().getNome()))
                        .toList()
        );
    }
}
