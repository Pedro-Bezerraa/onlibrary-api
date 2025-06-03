package com.onlibrary.onlibrary_api.service;

import com.onlibrary.onlibrary_api.dto.livro.LivroRequestDTO;
import com.onlibrary.onlibrary_api.dto.livro.LivroResponseDTO;
import com.onlibrary.onlibrary_api.exception.ConflictException;
import com.onlibrary.onlibrary_api.model.entities.Livro;
import com.onlibrary.onlibrary_api.repository.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LivroService {
    private final LivroRepository livroRepository;

//    public LivroResponseDTO criarLivro(LivroRequestDTO dto) {
//        boolean tituloExiste = livroRepository.existsByTitulo(dto.titulo());
//        boolean isbnExiste = livroRepository.existsByIsbn(dto.isbn());
//        if (tituloExiste || isbnExiste) {
//            throw new ConflictException("Título de livro já existente.");
//        }
//
//        Livro livro = new Livro();
//
//        livro.setTitulo(dto.titulo());
//        livro.setDescricao(dto.descricao());
//        livro.setAnoLancamento(dto.anoLancamento());
////        livro.setCapa();
//    }
}
