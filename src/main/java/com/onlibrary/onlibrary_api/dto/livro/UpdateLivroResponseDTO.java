package com.onlibrary.onlibrary_api.dto.livro;

import com.onlibrary.onlibrary_api.dto.autor.AutorResponseDTO;
import com.onlibrary.onlibrary_api.dto.categoria.CategoriaResponseDTO;
import com.onlibrary.onlibrary_api.dto.editora.EditoraResponseDTO;
import com.onlibrary.onlibrary_api.dto.genero.GeneroResponseDTO;

import java.util.List;
import java.util.UUID;

public record UpdateLivroResponseDTO(
        UUID id,
        String isbn,
        String titulo,
        String descricao,
        Integer anoLancamento,
        List<AutorResponseDTO> autores,
        List<CategoriaResponseDTO> categorias,
        List<GeneroResponseDTO> generos,
        List<EditoraResponseDTO> editoras
) {
}
