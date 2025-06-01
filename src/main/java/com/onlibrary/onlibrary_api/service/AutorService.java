package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.autor.AutorRequestDTO;
import com.onlibrary.onlibrary_api.exception.InvalidCredentialsException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.Autor;
import com.onlibrary.onlibrary_api.repository.AutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutorService {
    private final AutorRepository autorRepository;

    public void criarAutor(AutorRequestDTO dto) {
        boolean nomeExiste = autorRepository.existsByNome(dto.nome());
        if (nomeExiste) {
            throw new InvalidCredentialsException("Já existe um autor com esse nome");
        }

        Autor autor = new Autor();

        autor.setNome(dto.nome());

        autorRepository.save(autor);
    }

    public void atualizarAutor(UUID id, AutorRequestDTO dto) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado"));

        boolean nomeExiste = autorRepository.existsByNome(dto.nome());
        if (nomeExiste) {
            throw new InvalidCredentialsException("Já existe um autor com esse nome");
        }

        Autor autorAtualizado = new Autor();

        autorAtualizado.setNome(dto.nome());

        autorRepository.save(autorAtualizado);
    }
}
