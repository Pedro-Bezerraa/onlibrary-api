package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.livro.LivroHomePageSearchDTO;
import com.onlibrary.onlibrary_api.dto.autor.AutorResponseDTO;
import com.onlibrary.onlibrary_api.dto.categoria.CategoriaResponseDTO;
import com.onlibrary.onlibrary_api.dto.editora.EditoraResponseDTO;
import com.onlibrary.onlibrary_api.dto.genero.GeneroResponseDTO;
import com.onlibrary.onlibrary_api.dto.livro.UpdateLivroRequestDTO;
import com.onlibrary.onlibrary_api.dto.livro.UpdateLivroResponseDTO;
import com.onlibrary.onlibrary_api.dto.livro.LivroRequestDTO;
import com.onlibrary.onlibrary_api.dto.livro.LivroResponseDTO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
    public List<VwTableBibliotecaLivro> searchLivrosInBiblioteca(String value, String filter, UUID bibliotecaId) {
        if (value == null || value.trim().isEmpty()) {
            return vwTableBibliotecaLivroRepository.findByFkIdBiblioteca(bibliotecaId);
        }

        return switch (filter.toLowerCase()) {
            case "título" -> vwTableBibliotecaLivroRepository.searchByTituloInBiblioteca(bibliotecaId, value);
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
            case "título" -> vwLivroRepository.searchByTitulo(value);
            case "isbn" -> vwLivroRepository.searchByIsbn(value);
            case "autore" -> vwLivroRepository.searchByAutores(value);
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
    public LivroResponseDTO criarLivro(LivroRequestDTO dto, MultipartFile imagem) {
        if (livroRepository.existsByIsbnIgnoreCase(dto.isbn())) {
            throw new BusinessException("ISBN já cadastrado");
        }

        if (livroRepository.existsByTituloIgnoreCase(dto.titulo())) {
            throw new BusinessException("Título já cadastrado.");
        }

        String urlImagem = null;
        if (imagem != null && !imagem.isEmpty()) {
            urlImagem = supabaseStorageService.uploadImagem(imagem);
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
    public UpdateLivroResponseDTO atualizarLivro(UUID id, UpdateLivroRequestDTO dto, MultipartFile imagem) {
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Livro não encontrado"));

        if (imagem != null && !imagem.isEmpty()) {
            String novaImagem = supabaseStorageService.uploadImagem(imagem);
            livro.setCapa(novaImagem);
        }

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

        livroRepository.save(livro);

        List<LivroAutor> autores = List.of();
        List<LivroCategoria> categorias = List.of();
        List<LivroEditora> editoras = List.of();
        List<LivroGenero> generos = List.of();

        if (dto.autores() != null) {
            livroAutorRepository.deleteByLivroId(livro.getId());
            autores = dto.autores().stream()
                    .map(autorId -> {
                        Autor autor = autorRepository.findById(autorId)
                                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado"));
                        return LivroAutor.builder().livro(livro).autor(autor).build();
                    }).toList();
            livroAutorRepository.saveAll(autores);
        }

        if (dto.categorias() != null) {
            livroCategoriaRepository.deleteByLivroId(livro.getId());
            categorias = dto.categorias().stream()
                    .map(categoriaId -> {
                        Categoria categoria = categoriaRepository.findById(categoriaId)
                                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
                        return LivroCategoria.builder().livro(livro).categoria(categoria).build();
                    }).toList();
            livroCategoriaRepository.saveAll(categorias);
        }

        if (dto.editoras() != null) {
            livroEditoraRepository.deleteByLivroId(livro.getId());
            editoras = dto.editoras().stream()
                    .map(editoraId -> {
                        Editora editora = editoraRepository.findById(editoraId)
                                .orElseThrow(() -> new ResourceNotFoundException("Editora não encontrada"));
                        return LivroEditora.builder().livro(livro).editora(editora).build();
                    }).toList();
            livroEditoraRepository.saveAll(editoras);
        }

        if (dto.generos() != null) {
            livroGeneroRepository.deleteByLivroId(livro.getId());
            generos = dto.generos().stream()
                    .map(generoId -> {
                        Genero genero = generoRepository.findById(generoId)
                                .orElseThrow(() -> new ResourceNotFoundException("Gênero não encontrado"));
                        return LivroGenero.builder().livro(livro).genero(genero).build();
                    }).toList();
            livroGeneroRepository.saveAll(generos);
        }

        return new UpdateLivroResponseDTO(
                livro.getId(),
                livro.getIsbn(),
                livro.getTitulo(),
                livro.getDescricao(),
                livro.getAnoLancamento(),
                autores.stream()
                        .map(a -> new AutorResponseDTO(a.getAutor().getId(), a.getAutor().getNome()))
                        .toList(),
                categorias.stream()
                        .map(c -> new CategoriaResponseDTO(c.getCategoria().getId(), c.getCategoria().getNome()))
                        .toList(),
                generos.stream()
                        .map(g -> new GeneroResponseDTO(g.getGenero().getId(), g.getGenero().getNome()))
                        .toList(),
                editoras.stream()
                        .map(e -> new EditoraResponseDTO(e.getEditora().getId(), e.getEditora().getNome()))
                        .toList()
        );
    }
}
