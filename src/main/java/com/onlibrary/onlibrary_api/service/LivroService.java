package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.repository.AutorRepository;
import com.onlibrary.onlibrary_api.repository.CategoriaRepository;
import com.onlibrary.onlibrary_api.repository.EditoraRepository;
import com.onlibrary.onlibrary_api.repository.GeneroRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LivroService {
    private GeneroRepository generoRepository;
    private EditoraRepository editoraRepository;
    private CategoriaRepository categoriaRepository;
    private AutorRepository autorRepository;

//    public List<AutorDTO> listarAutores() {
//        return autorRepository.findAll().stream()
//                .map(autor -> new AutorDTO(autor.getId(), autor.getNome()))
//                .toList();
//    }
}
