package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.editora.*;
import com.onlibrary.onlibrary_api.exception.BusinessException;
import com.onlibrary.onlibrary_api.exception.ConflictException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.Editora;
import com.onlibrary.onlibrary_api.repository.entities.EditoraRepository;
import com.onlibrary.onlibrary_api.repository.entities.LivroEditoraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EditoraService {
    private final EditoraRepository editoraRepository;
    private final LivroEditoraRepository livroEditoraRepository;

    @Transactional(readOnly = true)
    public EditoraDependenciesDTO getEditoraDependencies(UUID id) {
        Editora editora = editoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editora não encontrada."));
        return new EditoraDependenciesDTO(editora.getNome());
    }

    @Transactional(readOnly = true)
    public List<EditoraResponseDTO> listarEditoras() {
        return editoraRepository.findByDeletadoFalse().stream()
                .map(editora -> new EditoraResponseDTO(editora.getId(), editora.getNome()))
                .collect(Collectors.toList());
    }

    @Transactional
    public EditoraResponseDTO criarEditora(EditoraRequestDTO dto) {
        boolean nomeExiste = editoraRepository.existsByNome(dto.nome());
        if (nomeExiste) {
            throw new ConflictException("Já existe uma editora com esse nome");
        }

        Editora editora = new Editora();

        editora.setNome(dto.nome());

        editoraRepository.save(editora);

        return new EditoraResponseDTO(editora.getId(), editora.getNome());
    }

    @Transactional
    public UpdateEditoraResponseDTO atualizar(UUID id, UpdateEditoraRequestDTO dto) {
        Editora editora = editoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editora não encontrada"));

        if (!editora.getNome().equalsIgnoreCase(dto.nome())
                && editoraRepository.existsByNome(dto.nome())) {
            throw new ConflictException("Já existe uma editora com esse nome");
        }

        editora.setNome(dto.nome());

        editoraRepository.save(editora);

        return new UpdateEditoraResponseDTO(editora.getId(), editora.getNome());
    }

    @Transactional
    public void deletarEditora(UUID id) {
        Editora editora = editoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editora não encontrada."));

        if (livroEditoraRepository.existsByEditoraIdAndLivroDeletadoIsFalse(id)) {
            throw new BusinessException("Não é possível excluir a editora, pois ela está associada a um ou mais livros ativos.");
        }

        editora.setDeletado(true);
        editoraRepository.save(editora);
    }
}
