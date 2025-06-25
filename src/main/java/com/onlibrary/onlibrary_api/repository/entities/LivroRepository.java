package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.dto.livro.LivroCategoriaResponseDTO;
import com.onlibrary.onlibrary_api.model.entities.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, UUID> {
    boolean existsByIsbnIgnoreCase(String isbn);
    boolean existsByTituloIgnoreCase(String titulo);
    long countByDeletadoFalse();
    List<Livro> findByDeletadoFalse();

    @Query("SELECT new com.onlibrary.onlibrary_api.dto.livro.LivroCategoriaResponseDTO(l.id, l.titulo, l.capa) " +
            "FROM Livro l JOIN l.categorias lc " +
            "WHERE lc.categoria.id = :categoriaId AND l.deletado = false")
    List<LivroCategoriaResponseDTO> findLivrosByCategoriaId(@Param("categoriaId") UUID categoriaId);
}
