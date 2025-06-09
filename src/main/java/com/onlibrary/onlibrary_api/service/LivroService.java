package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.autor.AutorResponseDTO;
import com.onlibrary.onlibrary_api.dto.categoria.CategoriaResponseDTO;
import com.onlibrary.onlibrary_api.dto.editora.EditoraResponseDTO;
import com.onlibrary.onlibrary_api.dto.genero.GeneroResponseDTO;
import com.onlibrary.onlibrary_api.dto.livro.LivroRequestDTO;
import com.onlibrary.onlibrary_api.dto.livro.LivroResponseDTO;
import com.onlibrary.onlibrary_api.exception.BusinessException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.*;
import com.onlibrary.onlibrary_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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
}
