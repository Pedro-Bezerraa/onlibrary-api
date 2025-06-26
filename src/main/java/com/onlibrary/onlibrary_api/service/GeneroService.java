package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.genero.GeneroDependenciesDTO;
import com.onlibrary.onlibrary_api.dto.genero.GeneroRequestDTO;
import com.onlibrary.onlibrary_api.dto.genero.GeneroResponseDTO;
import com.onlibrary.onlibrary_api.exception.BusinessException;
import com.onlibrary.onlibrary_api.exception.ConflictException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.Genero;
import com.onlibrary.onlibrary_api.repository.entities.GeneroRepository;
import com.onlibrary.onlibrary_api.repository.entities.LivroGeneroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeneroService {
    private final GeneroRepository generoRepository;
    private final LivroGeneroRepository livroGeneroRepository;

    @Transactional(readOnly = true)
    public List<GeneroResponseDTO> listarGeneros() {
        return generoRepository.findByDeletadoFalse().stream()
                .map(genero -> new GeneroResponseDTO(genero.getId(), genero.getNome()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GeneroDependenciesDTO getGeneroDependencies(UUID id) {
        Genero genero = generoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gênero não encontrado."));
        return new GeneroDependenciesDTO(genero.getNome());
    }

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

    @Transactional
    public void deletarGenero(UUID id) {
        Genero genero = generoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gênero não encontrado."));

        if (livroGeneroRepository.existsByGeneroIdAndLivroDeletadoIsFalse(id)) {
            throw new BusinessException("Não é possível excluir o gênero, pois ele está associado a um ou mais livros ativos.");
        }

        genero.setDeletado(true);
        generoRepository.save(genero);
    }
}
