package com.onlibrary.onlibrary_api.repository;

import com.onlibrary.onlibrary_api.model.entities.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {
    boolean existsByIsbnIgnoreCase(String isbn);
    boolean existsByTituloIgnoreCase(String titulo);

}
