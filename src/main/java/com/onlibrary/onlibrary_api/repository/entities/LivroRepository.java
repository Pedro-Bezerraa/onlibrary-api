package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.model.entities.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {
    boolean existsByIsbnIgnoreCase(String isbn);
    boolean existsByTituloIgnoreCase(String titulo);
    long countByDeletadoFalse();
    List<Livro> findByDeletadoFalse();
}
