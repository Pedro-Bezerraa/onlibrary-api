package com.onlibrary.onlibrary_api.repository.entities;

import com.onlibrary.onlibrary_api.model.entities.LivroAutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface LivroAutorRepository extends JpaRepository<LivroAutor, UUID> {
    void deleteByLivroId(UUID livroId);

    @Query("SELECT COUNT(la) > 0 FROM LivroAutor la WHERE la.autor.id = :autorId AND la.livro.deletado = false")
    boolean existsByAutorIdAndLivroDeletadoIsFalse(@Param("autorId") UUID autorId);
}
