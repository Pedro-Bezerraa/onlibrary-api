package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.model.entities.Editora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface EditoraRepository extends JpaRepository<Editora, UUID> {
    boolean existsByNome(String nome);
    long countByDeletadoFalse();
    List<Editora> findByDeletadoFalse();

    @Query("SELECT COUNT(DISTINCT e.id) FROM Editora e JOIN e.livros le JOIN le.livro l JOIN l.bibliotecaLivros bl WHERE bl.biblioteca.id = :bibliotecaId AND bl.deletado = false AND l.deletado = false AND e.deletado = false")
    long countByBibliotecaId(@Param("bibliotecaId") UUID bibliotecaId);
}
