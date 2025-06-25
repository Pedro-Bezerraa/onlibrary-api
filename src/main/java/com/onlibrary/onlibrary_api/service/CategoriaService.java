package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.categoria.UpdateCategoriaRequestDTO;
import com.onlibrary.onlibrary_api.dto.categoria.UpdateCategoriaResponseDTO;
import com.onlibrary.onlibrary_api.dto.categoria.CategoriaRequestDTO;
import com.onlibrary.onlibrary_api.dto.categoria.CategoriaResponseDTO;
import com.onlibrary.onlibrary_api.dto.livro.LivroCategoriaResponseDTO;
import com.onlibrary.onlibrary_api.exception.BusinessException;
import com.onlibrary.onlibrary_api.exception.ConflictException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.Categoria;
import com.onlibrary.onlibrary_api.repository.entities.CategoriaRepository;
import com.onlibrary.onlibrary_api.repository.entities.LivroCategoriaRepository;
import com.onlibrary.onlibrary_api.repository.entities.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final LivroCategoriaRepository livroCategoriaRepository;
    private final LivroRepository livroRepository;


    @Transactional(readOnly = true)
    public List<LivroCategoriaResponseDTO> listarLivrosPorCategoria(UUID categoriaId) {
        if (!categoriaRepository.existsById(categoriaId)) {
            throw new ResourceNotFoundException("Categoria não encontrada.");
        }
        return livroRepository.findLivrosByCategoriaId(categoriaId);
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarCategorias() {
        return categoriaRepository.findByDeletadoFalse().stream()
                .map(categoria -> new CategoriaResponseDTO(categoria.getId(), categoria.getNome()))
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoriaResponseDTO criarCategoria(CategoriaRequestDTO dto) {
        boolean nomeExiste = categoriaRepository.existsByNome(dto.nome());
        if (nomeExiste) {
            throw new ConflictException("Já existe uma Categoria com esse nome");
        }

        Categoria categoria = new Categoria();

        categoria.setNome(dto.nome());

        categoriaRepository.save(categoria);

        return new CategoriaResponseDTO(categoria.getId(), categoria.getNome());
    }

    @Transactional
    public UpdateCategoriaResponseDTO atualizarCategoria(UUID id, UpdateCategoriaRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        boolean nomeExiste = categoriaRepository.existsByNome(dto.nome());
        if (nomeExiste) {
            throw new ConflictException("Já existe uma categoria com esse nome");
        }

        categoria.setNome(dto.nome());

        categoriaRepository.save(categoria);

        return new UpdateCategoriaResponseDTO(categoria.getId(), categoria.getNome());
    }

    @Transactional
    public void deletarCategoria(UUID id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada."));

        if (livroCategoriaRepository.existsByCategoriaIdAndLivroDeletadoIsFalse(id)) {
            throw new BusinessException("Não é possível excluir a categoria, pois ela está associada a um ou mais livros.");
        }

        categoria.setDeletado(true);
        categoriaRepository.save(categoria);
    }
}
