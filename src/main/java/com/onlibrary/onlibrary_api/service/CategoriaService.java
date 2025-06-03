package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.categoria.CategoriaRequestDTO;
import com.onlibrary.onlibrary_api.exception.ConflictException;
import com.onlibrary.onlibrary_api.exception.InvalidCredentialsException;
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

    public void criarCategoria(CategoriaRequestDTO dto) {
        boolean nomeExiste = categoriaRepository.existsByNome(dto.nome());
        if (nomeExiste) {
            throw new ConflictException("Já existe uma Categoria com esse nome");
        }

        Categoria categoria = new Categoria();

        categoria.setNome(dto.nome());

        categoriaRepository.save(categoria);
    }

    public void atualizarCategoria(UUID id, CategoriaRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        boolean nomeExiste = categoriaRepository.existsByNome(dto.nome());
        if (nomeExiste) {
            throw new ConflictException("Já existe uma categoria com esse nome");
        }

        categoria.setNome(dto.nome());

        categoriaRepository.save(categoria);
    }
}
