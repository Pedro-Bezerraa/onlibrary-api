package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.categoria.AttCategoriaRequestDTO;
import com.onlibrary.onlibrary_api.dto.categoria.AttCategoriaResponseDTO;
import com.onlibrary.onlibrary_api.dto.categoria.CategoriaRequestDTO;
import com.onlibrary.onlibrary_api.dto.categoria.CategoriaResponseDTO;
import com.onlibrary.onlibrary_api.exception.ConflictException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.Categoria;
import com.onlibrary.onlibrary_api.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

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

    public AttCategoriaResponseDTO atualizarCategoria(UUID id, AttCategoriaRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        boolean nomeExiste = categoriaRepository.existsByNome(dto.nome());
        if (nomeExiste) {
            throw new ConflictException("Já existe uma categoria com esse nome");
        }

        categoria.setNome(dto.nome());

        categoriaRepository.save(categoria);

        return new AttCategoriaResponseDTO(categoria.getId(), categoria.getNome());
    }
}
