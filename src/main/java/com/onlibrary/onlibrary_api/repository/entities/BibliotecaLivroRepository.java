package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.model.entities.BibliotecaLivro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BibliotecaLivroRepository extends JpaRepository<BibliotecaLivro, UUID> {
    boolean existsByBibliotecaIdAndLivroId(UUID bibliotecaId, UUID livroId);
    long countByBibliotecaId(UUID bibliotecaID);
    long countByBibliotecaIdAndDeletadoFalse(UUID bibliotecaId);
    @Query("SELECT bl FROM BibliotecaLivro bl JOIN FETCH bl.livro l WHERE bl.biblioteca.id = :bibliotecaId AND bl.deletado = false AND l.deletado = false")
    List<BibliotecaLivro> findWithLivroByBibliotecaId(@Param("bibliotecaId") UUID bibliotecaId);
}
