package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.autor.AutorRequestDTO;
import com.onlibrary.onlibrary_api.dto.autor.AutorResponseDTO;
import com.onlibrary.onlibrary_api.exception.ConflictException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.Autor;
import com.onlibrary.onlibrary_api.repository.AutorRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AutorService {
    private final AutorRepository autorRepository;

    @Transactional
    public AutorResponseDTO criarAutor(AutorRequestDTO dto) {
        if (autorRepository.existsByNome(dto.nome())) {
            throw new ConflictException("Já existe um autor com esse nome.");
        }

        Autor autor = new Autor();
        autor.setNome(dto.nome());

        autorRepository.save(autor);

        return new AutorResponseDTO(autor.getId(), autor.getNome());
    }

    @Transactional
    public AutorResponseDTO atualizarAutor(UUID id, AutorRequestDTO dto) {
        Autor autor = autorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Autor não encontrado."));

        boolean nomeExiste = autorRepository.existsByNome(dto.nome());
        if (nomeExiste && !autor.getNome().equalsIgnoreCase(dto.nome())) {
            throw new ConflictException("Já existe um autor com esse nome.");
        }

        autor.setNome(dto.nome());

        autorRepository.save(autor);

        return new AutorResponseDTO(autor.getId(), autor.getNome());
    }
}
