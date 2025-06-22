package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.genero.GeneroRequestDTO;
import com.onlibrary.onlibrary_api.dto.genero.GeneroResponseDTO;
import com.onlibrary.onlibrary_api.exception.ConflictException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.Genero;
import com.onlibrary.onlibrary_api.repository.entities.GeneroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GeneroService {
    private final GeneroRepository generoRepository;

    @Transactional
    public GeneroResponseDTO criarGenero(GeneroRequestDTO dto) {
        if (generoRepository.existsByNome(dto.nome())) {
            throw new ConflictException("Já existe um gênero com esse nome");
        }

        Genero genero = new Genero();
        genero.setNome(dto.nome());

        generoRepository.save(genero);

        return new GeneroResponseDTO(genero.getId(), genero.getNome());
    }


    @Transactional
    public GeneroResponseDTO atualizarGenero(UUID id, GeneroRequestDTO dto) {
        Genero genero = generoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gênero não encontrado"));

        boolean nomeExiste = generoRepository.existsByNome(dto.nome());
        if (nomeExiste && !genero.getNome().equalsIgnoreCase(dto.nome())) {
            throw new ConflictException("Já existe um gênero com esse nome");
        }

        genero.setNome(dto.nome());

        generoRepository.save(genero);

        return new GeneroResponseDTO(genero.getId(), genero.getNome());
    }

}
