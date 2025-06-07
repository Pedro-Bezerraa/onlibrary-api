package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.editora.AttEditoraRequestDTO;
import com.onlibrary.onlibrary_api.dto.editora.AttEditoraResponseDTO;
import com.onlibrary.onlibrary_api.dto.editora.EditoraRequestDTO;
import com.onlibrary.onlibrary_api.dto.editora.EditoraResponseDTO;
import com.onlibrary.onlibrary_api.exception.ConflictException;
import com.onlibrary.onlibrary_api.exception.InvalidCredentialsException;
import com.onlibrary.onlibrary_api.exception.ResourceNotFoundException;
import com.onlibrary.onlibrary_api.model.entities.Editora;
import com.onlibrary.onlibrary_api.repository.EditoraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EditoraService {
    private final EditoraRepository editoraRepository;

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

    public AttEditoraResponseDTO atualizar(UUID id, AttEditoraRequestDTO dto) {
        Editora editora = editoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Editora não encontrada"));

        if (!editora.getNome().equalsIgnoreCase(dto.nome())
                && editoraRepository.existsByNome(dto.nome())) {
            throw new ConflictException("Já existe uma editora com esse nome");
        }

        editora.setNome(dto.nome());

        editoraRepository.save(editora);

        return new AttEditoraResponseDTO(editora.getId(), editora.getNome());
    }
}
