package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.model.entities.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface GeneroRepository extends JpaRepository<Genero, UUID> {
    boolean existsByNome(String nome);
    long countByDeletadoFalse();
    List<Genero> findByDeletadoFalse();

    @Query("SELECT COUNT(DISTINCT c.id) FROM Categoria c JOIN c.livros lc JOIN lc.livro l JOIN l.bibliotecaLivros bl WHERE bl.biblioteca.id = :bibliotecaId AND bl.deletado = false AND l.deletado = false AND c.deletado = false")
    long countByBibliotecaId(@Param("bibliotecaId") UUID bibliotecaId);
}
