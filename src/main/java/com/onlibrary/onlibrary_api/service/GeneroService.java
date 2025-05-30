package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.GeneroRequestDTO;
import com.onlibrary.onlibrary_api.exception.InvalidCredentialsException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.Genero;
import com.onlibrary.onlibrary_api.repository.GeneroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GeneroService {
    private final GeneroRepository generoRepository;

    public void criarGenero(GeneroRequestDTO dto) {
        boolean nomeExiste = generoRepository.existsByNome(dto.nome());
        if (nomeExiste) {
            throw new InvalidCredentialsException("Já existe um genero com esse nome");
        }

        Genero genero = new Genero();

        genero.setNome(dto.nome());

        generoRepository.save(genero );
    }

    public void atualizarGenero(UUID id, GeneroRequestDTO dto) {
        Genero genero = generoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genero não encontrado"));

        boolean nomeExiste = generoRepository.existsByNome(dto.nome());
        if (nomeExiste) {
            throw new InvalidCredentialsException("Já existe um genero com esse nome");
        }

        Genero generoAtualizado = new Genero();

        generoAtualizado.setNome(dto.nome());

        generoRepository.save(generoAtualizado);
    }
}
