package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.model.entities.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AutorRepository extends JpaRepository<Autor, UUID> {
    boolean existsByNome(String nome);
    long countByDeletadoFalse();
    List<Autor> findByDeletadoFalse();
    @Query("SELECT COUNT(DISTINCT a.id) FROM Autor a JOIN a.livros la JOIN la.livro l JOIN l.bibliotecaLivros bl WHERE bl.biblioteca.id = :bibliotecaId AND bl.deletado = false AND l.deletado = false AND a.deletado = false")
    long countByBibliotecaId(@Param("bibliotecaId") UUID bibliotecaId);
}
